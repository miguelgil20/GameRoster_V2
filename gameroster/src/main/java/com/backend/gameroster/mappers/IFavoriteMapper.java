package com.backend.gameroster.mappers;

import com.backend.gameroster.dto.favorite.FavoriteDTO;
import com.backend.gameroster.entity.Favorite;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface IFavoriteMapper {

    @Mapping(target = "userId", source = "user.code")
    @Mapping(target = "teamId", source = "team.id")
    @Mapping(target = "teamName", source = "team.name")
    @Mapping(target = "region", source = "team.region")
    @Mapping(target = "ranking", source = "team.ranking")
    FavoriteDTO toDTO(Favorite favorite);

    List<FavoriteDTO> toDTOList(List<Favorite> favorites);
}