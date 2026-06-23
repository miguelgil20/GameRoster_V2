package com.backend.gameroster.config;


import com.backend.gameroster.entity.Role;
import com.backend.gameroster.enums.RoleType;
import com.backend.gameroster.repository.role.IRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InizialiteData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InizialiteData.class);


    private final IRoleRepository roleRepository;

    public InizialiteData(IRoleRepository roleRepository) {

        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        try {
            logger.info("Iniciando insercion de datos iniciales...");

            crearRoleSiNoExiste(RoleType.ADMIN);
            crearRoleSiNoExiste(RoleType.USER);

            logger.info("Insercion de datos iniciales finalizada");

        } catch (Exception e) {

            logger.error("Error en inicialización de datos: {}", e.getMessage(), e);

        }
    }



    private void crearRoleSiNoExiste(RoleType rol) {
        if (!roleRepository.existsByRol(rol)) {
            roleRepository.save(new Role(null, rol));
            logger.info("Role añadido: {}", rol);
        }
    }
}
