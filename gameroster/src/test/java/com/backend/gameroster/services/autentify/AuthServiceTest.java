package com.backend.gameroster.services.autentify;

import com.backend.gameroster.config.security.JwtTokenProvider;
import com.backend.gameroster.dto.user.CreateUseroDTO;
import com.backend.gameroster.dto.user.LoginUserDTO;
import com.backend.gameroster.entity.Role;
import com.backend.gameroster.entity.User;
import com.backend.gameroster.enums.RoleType;
import com.backend.gameroster.exception.ErrorGenericoException;
import com.backend.gameroster.mappers.UsuarioMapper;
import com.backend.gameroster.repository.user.IUserRepository;
import com.backend.gameroster.services.role.IRoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRoleService roleService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_whenUserIsValid_shouldSaveUser() {
        CreateUseroDTO dto = new CreateUseroDTO("miguel", "Password1", "miguel@email.com");
        User user = user();
        Role role = role();

        when(userRepository.existsByUsername("miguel")).thenReturn(false);
        when(userRepository.existsByEmail("miguel@email.com")).thenReturn(false);
        when(usuarioMapper.toUsuarioFromCreateUsuarioDTO(dto)).thenReturn(user);
        when(passwordEncoder.encode("Password1")).thenReturn("encoded-password");
        when(roleService.obtenerRol(RoleType.USER)).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);

        authService.register(dto);

        assertEquals("miguel", user.getUsername());
        assertEquals("encoded-password", user.getPassword());
        assertEquals(1, user.getRoles().size());
        verify(userRepository).save(user);
    }

    @Test
    void register_whenUsernameExists_shouldThrowErrorGenerico() {
        CreateUseroDTO dto = new CreateUseroDTO("miguel", "Password1", "miguel@email.com");
        when(userRepository.existsByUsername("miguel")).thenReturn(true);

        assertThrows(ErrorGenericoException.class, () -> authService.register(dto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_whenCredentialsAreValid_shouldReturnToken() {
        LoginUserDTO dto = new LoginUserDTO("miguel", "Password1");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtTokenProvider.generateToken(userDetails)).thenReturn("jwt-token");

        String token = authService.login(dto);

        assertEquals("jwt-token", token);
    }

    @Test
    void login_whenAuthenticationFails_shouldThrowErrorGenerico() {
        LoginUserDTO dto = new LoginUserDTO("miguel", "bad-password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("bad credentials"));

        assertThrows(ErrorGenericoException.class, () -> authService.login(dto));
    }

    private User user() {
        User user = new User();
        user.setCode(1L);
        user.setUsername("miguel");
        user.setEmail("miguel@email.com");
        user.setPassword("plain-password");
        user.setCreationDate(LocalDateTime.now());
        user.setRoles(List.of());
        return user;
    }

    private Role role() {
        Role role = new Role();
        role.setId(1L);
        role.setRol(RoleType.USER);
        return role;
    }
}
