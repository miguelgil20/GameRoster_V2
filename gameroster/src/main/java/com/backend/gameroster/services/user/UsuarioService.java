package com.backend.gameroster.services.user;

import com.backend.gameroster.entity.User;
import com.backend.gameroster.exception.NotFoundEntityException;
import com.backend.gameroster.repository.user.IUserRepository;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService implements IUsuarioService {

    private static final Logger logger =
            LoggerFactory.getLogger(UsuarioService.class);

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        logger.info("Buscando usuario {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {

                    logger.error("Usuario {} no encontrado", username);

                    return new NotFoundEntityException(
                            "Usuario " + username + " no encontrado"
                    );
                });

        logger.info("Usuario {} encontrado correctamente", username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
    }
}