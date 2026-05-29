package com.backend.gameroster.mappers;

import com.backend.gameroster.dto.user.CreateUseroDTO;
import com.backend.gameroster.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UsuarioMapper {

    @Mapping(target = "roles", ignore = true)
    User toUsuarioFromCreateUsuarioDTO( CreateUseroDTO dto);

    CreateUseroDTO toCrearUsuarioDTOFromUsuario(User usuario);
}