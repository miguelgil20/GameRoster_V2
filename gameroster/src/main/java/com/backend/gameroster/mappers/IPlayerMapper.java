package com.backend.gameroster.mappers;

import com.backend.gameroster.dto.player.PlayerCreateDTO;
import com.backend.gameroster.dto.player.PlayerDTO;
import com.backend.gameroster.entity.Player;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface IPlayerMapper {

    @Mapping(target = "teamId", source = "team.id")
    @Mapping(target = "teamName", source = "team.name")
    PlayerDTO toDTO(Player player);

    @Mapping(target = "team", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Player toEntity(PlayerCreateDTO playerCreateDTO);

    List<PlayerDTO> toDTOList(List<Player> players);

    List<Player> toEntityList(List<PlayerCreateDTO> playersDTO);
}