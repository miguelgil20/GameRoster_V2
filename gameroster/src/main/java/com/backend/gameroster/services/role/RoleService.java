package com.backend.gameroster.services.role;


import com.backend.gameroster.entity.Role;
import com.backend.gameroster.enums.RoleType;
import com.backend.gameroster.exception.NotFoundEntityException;
import com.backend.gameroster.repository.role.IRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;

    @Override
    public Role obtenerRol(RoleType rol) {
        return roleRepository.findByRol(rol)
                .orElseThrow(() ->
                        new NotFoundEntityException("Rol " + rol + " no encontrado")
                );
    }
}