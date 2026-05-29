package com.backend.gameroster.services.role;


import com.backend.gameroster.entity.Role;
import com.backend.gameroster.enums.RoleType;

public interface IRoleService {

    Role obtenerRol(RoleType rol);
}