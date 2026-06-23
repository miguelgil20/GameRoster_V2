package com.backend.gameroster.services.favorite;

import com.backend.gameroster.dto.favorite.FavoriteCreateDTO;
import com.backend.gameroster.dto.favorite.FavoriteDTO;
import com.backend.gameroster.entity.Favorite;
import com.backend.gameroster.entity.Team;
import com.backend.gameroster.entity.User;
import com.backend.gameroster.exception.*;
import com.backend.gameroster.mappers.IFavoriteMapper;
import com.backend.gameroster.repository.favorite.IFavoriteRepository;
import com.backend.gameroster.repository.team.ITeamRepository;
import com.backend.gameroster.repository.user.IUserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteService implements IFavoriteService {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);

    private final IFavoriteRepository favoriteRepository;
    private final IUserRepository userRepository;
    private final ITeamRepository teamRepository;
    private final IFavoriteMapper favoriteMapper;

    public FavoriteService(
            IFavoriteRepository favoriteRepository,
            IUserRepository userRepository,
            ITeamRepository teamRepository,
            IFavoriteMapper favoriteMapper
    ) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    public List<FavoriteDTO> findAll() {
        try {

            List<Favorite> favorites = (List<Favorite>) favoriteRepository.findAll();

            return favoriteMapper.toDTOList(favorites);

        } catch (Exception e) {
            throw new ErrorGenericoException("Error al buscar todos los favoritos", e);
        }
    }

    @Override
    public FavoriteDTO findById(Long id) {
        try {

            Favorite favorite = favoriteRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException("No se encontró el favorito con id: " + id)
                    );

            return favoriteMapper.toDTO(favorite);

        } catch (NotFoundEntityException e) {
            throw e;

        } catch (Exception e) {
            throw new ErrorGenericoException("Error al buscar el favorito con id: " + id, e);
        }
    }

    @Override
    public FavoriteDTO save(FavoriteCreateDTO dto) {
        try {

            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() ->
                            new NotFoundEntityException("No se encontró el usuario con id: " + dto.getUserId())
                    );

            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() ->
                            new NotFoundEntityException("No se encontró el equipo con id: " + dto.getTeamId())
                    );

            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setTeam(team);
            favorite.setCreatedAt(LocalDateTime.now());

            Favorite saved = favoriteRepository.save(favorite);

            return favoriteMapper.toDTO(saved);

        } catch (NotFoundEntityException e) {
            throw e;

        } catch (Exception e) {
            throw new CreateEntityException("Favorite", dto, e);
        }
    }

    @Override
    public FavoriteDTO update(Long id, FavoriteCreateDTO dto) {
        try {

            Favorite favoriteUpdate = favoriteRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException("No se encontró el favorito con id: " + id)
                    );

            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() ->
                            new NotFoundEntityException("No se encontró el usuario con id: " + dto.getUserId())
                    );

            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() ->
                            new NotFoundEntityException("No se encontró el equipo con id: " + dto.getTeamId())
                    );

            favoriteUpdate.setUser(user);
            favoriteUpdate.setTeam(team);

            Favorite saved = favoriteRepository.save(favoriteUpdate);

            return favoriteMapper.toDTO(saved);

        } catch (NotFoundEntityException e) {
            throw e;

        } catch (Exception e) {
            throw new UpdateEntityException("Favorite", dto, e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {

            Favorite favorite = favoriteRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException("No se encontró el favorito con id: " + id)
                    );

            favoriteRepository.delete(favorite);


        } catch (NotFoundEntityException e) {
            throw e;

        } catch (Exception e) {
            throw new DeleteEntityException("Favorite", id, e);
        }
    }

    @Override
    public List<FavoriteDTO> findFavoriteTeamsByUserId(Long userId) {
        try {

            List<Favorite> favorites = favoriteRepository.findFavoritesByUserId(userId);

            return favoriteMapper.toDTOList(favorites);

        } catch (Exception e) {
            throw new ErrorGenericoException(
                    "Error al buscar equipos favoritos del usuario con id: " + userId,
                    e
            );
        }
    }
}