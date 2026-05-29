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

            logger.info("Buscando todos los jugadores");

            List<Player> players = (List<Player>) playerRepository.findAll();

            return playerMapper.toDTOList(players);

        } catch (Exception e) {

            logger.error("Error al buscar todos los jugadores", e);

            throw new ErrorGenericoException(
                    "Error al buscar todos los jugadores",
                    e
            );
        }
    }

    @Override
    public PlayerDTO findById(Long id) {
        try {

            logger.info("Buscando jugador con id: {}", id);

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

            logger.error("Error al buscar el jugador con id: {}", id, e);

            throw new ErrorGenericoException(
                    "Error al buscar el jugador con id: " + id,
                    e
            );
        }
    }

    @Override
    public PlayerDTO save(PlayerCreateDTO playerCreateDTO) {
        try {

            logger.info("Creando nuevo jugador: {}", playerCreateDTO.getNickname());

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

            logger.info("Jugador creado correctamente con id: {}", playerSaved.getId());

            return playerMapper.toDTO(playerSaved);

        } catch (NotFoundEntityException e) {

            throw e;

        } catch (Exception e) {

            logger.error("Error al crear el jugador: {}", playerCreateDTO, e);

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

            logger.info("Actualizando jugador con id: {}", id);

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

            logger.info("Jugador actualizado correctamente con id: {}", id);

            return playerMapper.toDTO(playerSaved);

        } catch (NotFoundEntityException e) {

            logger.warn("No se puede actualizar. Jugador o equipo no encontrado");

            throw e;

        } catch (Exception e) {

            logger.error("Error al actualizar el jugador con id: {}", id, e);

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

            logger.info("Eliminando jugador con id: {}", id);

            Player player = playerRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException(
                                    "No se encontró el jugador con id: " + id
                            )
                    );

            playerRepository.delete(player);

            logger.info("Jugador eliminado correctamente con id: {}", id);

        } catch (NotFoundEntityException e) {

            logger.warn("No se puede eliminar. Jugador no encontrado con id: {}", id);

            throw e;

        } catch (Exception e) {

            logger.error("Error al eliminar el jugador con id: {}", id, e);

            throw new DeleteEntityException(
                    "Player",
                    id,
                    e
            );
        }
    }
}