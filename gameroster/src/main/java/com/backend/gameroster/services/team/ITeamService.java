package com.backend.gameroster.services.team;

import com.backend.gameroster.dto.team.TeamDTO;

import java.util.List;

public interface ITeamService {

    List<TeamDTO> findAll();

    TeamDTO findById(Long id);

    TeamDTO save(TeamDTO teamDTO);

    TeamDTO update(Long id, TeamDTO teamDTO);

    void deleteById(Long id);
}