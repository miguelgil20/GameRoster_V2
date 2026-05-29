package com.backend.gameroster.services.autentify;


import com.backend.gameroster.config.security.JwtTokenProvider;
import com.backend.gameroster.dto.user.CreateUseroDTO;
import com.backend.gameroster.dto.user.LoginUserDTO;
import com.backend.gameroster.entity.Role;
import com.backend.gameroster.entity.User;
import com.backend.gameroster.enums.RoleType;
import com.backend.gameroster.exception.CreateEntityException;
import com.backend.gameroster.exception.ErrorGenericoException;
import com.backend.gameroster.mappers.UsuarioMapper;
import com.backend.gameroster.repository.user.IUserRepository;
import com.backend.gameroster.services.role.IRoleService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final IRoleService roleService;
    private final UsuarioMapper usuarioMapper;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Transactional
    @Override
    public void register(CreateUseroDTO dto) {

        logger.info("Registrando usuario {}", dto.getNombreUsuario());

        try {

            validateUser(dto);

            User user = usuarioMapper.toUsuarioFromCreateUsuarioDTO(dto);

            user.setUsername(dto.getNombreUsuario());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setCreationDate(LocalDateTime.now());

            Role roleUser = roleService.obtenerRol(RoleType.USER);
            user.setRoles(List.of(roleUser));

            userRepository.save(user);

            logger.info("Usuario registrado correctamente {}", user.getUsername());

        } catch (ErrorGenericoException ex) {

            throw ex;

        } catch (Exception ex) {

            logger.error("Error registrando usuario {}", dto.getNombreUsuario(), ex);

            throw new CreateEntityException(
                    User.class.getSimpleName(),
                    dto,
                    ex
            );
        }
    }

    private void validateUser(CreateUseroDTO dto) {

        if (userRepository.existsByUsername(dto.getNombreUsuario())) {
            throw new ErrorGenericoException(
                    "El nombre de usuario ya existe",
                    null
            );
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ErrorGenericoException(
                    "El email ya existe",
                    null
            );
        }
    }

    @Override
    public String login(LoginUserDTO dto) {

        logger.info("Intentando login usuario {}", dto.getNombreUsuario());

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    dto.getNombreUsuario(),
                                    dto.getPassword()
                            )
                    );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtTokenProvider.generateToken(userDetails);

            logger.info("Login correcto usuario {}", dto.getNombreUsuario());

            return token;

        } catch (Exception ex) {

            logger.error("Error login usuario {}", dto.getNombreUsuario(), ex);

            throw new ErrorGenericoException(
                    "Usuario o contraseña incorrectos",
                    ex
            );
        }

    }
}