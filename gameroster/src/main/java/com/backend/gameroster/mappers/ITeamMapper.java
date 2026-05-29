package com.backend.gameroster.mappers;

import com.backend.gameroster.dto.team.TeamDTO;
import com.backend.gameroster.entity.Team;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {IPlayerMapper.class}
)
public interface ITeamMapper {

    TeamDTO toDTO(Team team);

    Team toEntity(TeamDTO teamDTO);

    List<TeamDTO> toDTOList(List<Team> teams);

    List<Team> toEntityList(List<TeamDTO> teamsDTO);
}