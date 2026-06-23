package com.backend.gameroster.services.role;

import com.backend.gameroster.entity.Role;
import com.backend.gameroster.enums.RoleType;
import com.backend.gameroster.exception.NotFoundEntityException;
import com.backend.gameroster.repository.role.IRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private IRoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void obtenerRol_whenRoleExists_shouldReturnRole() {
        Role role = new Role();
        role.setId(1L);
        role.setRol(RoleType.USER);

        when(roleRepository.findByRol(RoleType.USER)).thenReturn(Optional.of(role));

        Role result = roleService.obtenerRol(RoleType.USER);

        assertEquals(RoleType.USER, result.getRol());
    }

    @Test
    void obtenerRol_whenRoleDoesNotExist_shouldThrowNotFound() {
        when(roleRepository.findByRol(RoleType.USER)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> roleService.obtenerRol(RoleType.USER));
    }
}
