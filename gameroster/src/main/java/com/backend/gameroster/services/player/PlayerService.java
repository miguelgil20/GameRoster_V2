package com.backend.gameroster.services.player;

import com.backend.gameroster.dto.player.PlayerCreateDTO;
import com.backend.gameroster.dto.player.PlayerDTO;
import com.backend.gameroster.entity.Player;
import com.backend.gameroster.entity.Team;
import com.backend.gameroster.exception.CreateEntityException;
import com.backend.gameroster.exception.DeleteEntityException;
import com.backend.gameroster.exception.ErrorGenericoException;
import com.backend.gameroster.exception.NotFoundEntityException;
import com.backend.gameroster.exception.UpdateEntityException;
import com.backend.gameroster.mappers.IPlayerMapper;
import com.backend.gameroster.repository.player.IPlayerRepository;
import com.backend.gameroster.repository.team.ITeamRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlayerService implements IPlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final IPlayerRepository playerRepository;
    private final ITeamRepository teamRepository;
    private final IPlayerMapper playerMapper;

    public PlayerService(
            IPlayerRepository playerRepository,
            ITeamRepository teamRepository,
            IPlayerMapper playerMapper
    ) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    public List<PlayerDTO> findAll() {
        try {

            List<Player> players = (List<Player>) playerRepository.findAll();

            return playerMapper.toDTOList(players);

        } catch (Exception e) {

            throw new ErrorGenericoException(
                    "Error al buscar todos los jugadores",
                    e
            );
        }
    }

    @Override
    public PlayerDTO findById(Long id) {
        try {

            Player player = playerRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException(
                                    "No se encontró el jugador con id: " + id
                            )
                    );

            return playerMapper.toDTO(player);

        } catch (NotFoundEntityException e) {

            throw e;

        } catch (Exception e) {

            throw new ErrorGenericoException(
                    "Error al buscar el jugador con id: " + id,
                    e
            );
        }
    }

    @Override
    public PlayerDTO save(PlayerCreateDTO playerCreateDTO) {
        try {

            Team team = teamRepository.findById(playerCreateDTO.getTeamId())
                    .orElseThrow(() ->
                            new NotFoundEntityException(
                                    "No se encontró el equipo con id: "
                                            + playerCreateDTO.getTeamId()
                            )
                    );

            Player player = playerMapper.toEntity(playerCreateDTO);

            player.setCreatedAt(LocalDateTime.now());
            player.setTeam(team);

            Player playerSaved = playerRepository.save(player);

            return playerMapper.toDTO(playerSaved);

        } catch (NotFoundEntityException e) {

            throw e;

        } catch (Exception e) {

            throw new CreateEntityException(
                    "Player",
                    playerCreateDTO,
                    e
            );
        }
    }

    @Override
    public PlayerDTO update(Long id, PlayerCreateDTO playerCreateDTO) {
        try {

            Player playerUpdate = playerRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException(
                                    "No se encontró el jugador con id: " + id
                            )
                    );

            Team team = teamRepository.findById(playerCreateDTO.getTeamId())
                    .orElseThrow(() ->
                            new NotFoundEntityException(
                                    "No se encontró el equipo con id: "
                                            + playerCreateDTO.getTeamId()
                            )
                    );

            playerUpdate.setNickname(playerCreateDTO.getNickname());
            playerUpdate.setName(playerCreateDTO.getName());
            playerUpdate.setRole(playerCreateDTO.getRole());
            playerUpdate.setCountry(playerCreateDTO.getCountry());
            playerUpdate.setTeam(team);

            Player playerSaved = playerRepository.save(playerUpdate);

            return playerMapper.toDTO(playerSaved);

        } catch (NotFoundEntityException e) {

            throw e;

        } catch (Exception e) {

            throw new UpdateEntityException(
                    "Player",
                    playerCreateDTO,
                    e
            );
        }
    }

    @Override
    public void deleteById(Long id) {
        try {

            Player player = playerRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException(
                                    "No se encontró el jugador con id: " + id
                            )
                    );

            playerRepository.delete(player);

        } catch (NotFoundEntityException e) {

            throw e;

        } catch (Exception e) {

            throw new DeleteEntityException(
                    "Player",
                    id,
                    e
            );
        }
    }
}