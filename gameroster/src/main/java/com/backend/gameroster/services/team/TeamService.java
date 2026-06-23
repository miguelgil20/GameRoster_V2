package com.backend.gameroster.services.team;

import com.backend.gameroster.dto.team.TeamDTO;
import com.backend.gameroster.entity.Team;
import com.backend.gameroster.exception.CreateEntityException;
import com.backend.gameroster.exception.DeleteEntityException;
import com.backend.gameroster.exception.ErrorGenericoException;
import com.backend.gameroster.exception.NotFoundEntityException;
import com.backend.gameroster.exception.UpdateEntityException;
import com.backend.gameroster.mappers.ITeamMapper;
import com.backend.gameroster.repository.team.ITeamRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeamService implements ITeamService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final ITeamRepository teamRepository;
    private final ITeamMapper teamMapper;

    public TeamService(
            ITeamRepository teamRepository,
            ITeamMapper teamMapper
    ) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    @Override
    public List<TeamDTO> findAll() {
        try {

            List<Team> teams = (List<Team>) teamRepository.findAll();

            return teamMapper.toDTOList(teams);

        } catch (Exception e) {
            throw new ErrorGenericoException("Error al buscar todos los equipos", e);
        }
    }

    @Override
    public TeamDTO findById(Long id) {
        try {

            Team team = teamRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException("No se encontró el equipo con id: " + id)
                    );

            return teamMapper.toDTO(team);

        } catch (NotFoundEntityException e) {
            throw e;

        } catch (Exception e) {
            throw new ErrorGenericoException("Error al buscar el equipo con id: " + id, e);
        }
    }

    @Override
    public TeamDTO save(TeamDTO teamDTO) {
        try {
            logger.info("Creando nuevo equipo: {}", teamDTO.getName());

            Team team = teamMapper.toEntity(teamDTO);
            team.setCreatedAt(LocalDateTime.now());

            Team teamSaved = teamRepository.save(team);

            return teamMapper.toDTO(teamSaved);

        } catch (Exception e) {
            throw new CreateEntityException("Team", teamDTO, e);
        }
    }

    @Override
    public TeamDTO update(Long id, TeamDTO teamDTO) {
        try {

            Team teamUpdate = teamRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException("No se encontró el equipo con id: " + id)
                    );

            teamUpdate.setName(teamDTO.getName());
            teamUpdate.setRegion(teamDTO.getRegion());
            teamUpdate.setRanking(teamDTO.getRanking());

            Team teamSaved = teamRepository.save(teamUpdate);

            return teamMapper.toDTO(teamSaved);

        } catch (NotFoundEntityException e) {
            throw e;

        } catch (Exception e) {
            throw new UpdateEntityException("Team", teamDTO, e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {

            Team team = teamRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException("No se encontró el equipo con id: " + id)
                    );

            teamRepository.delete(team);

        } catch (NotFoundEntityException e) {
            throw e;

        } catch (Exception e) {
            throw new DeleteEntityException("Team", id, e);
        }
    }
}