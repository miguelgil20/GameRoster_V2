package com.backend.gameroster.config;

import com.backend.gameroster.entity.Player;
import com.backend.gameroster.entity.Role;
import com.backend.gameroster.entity.Team;
import com.backend.gameroster.enums.RoleType;
import com.backend.gameroster.repository.player.IPlayerRepository;
import com.backend.gameroster.repository.role.IRoleRepository;
import com.backend.gameroster.repository.team.ITeamRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class InizialiteData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InizialiteData.class);

    private final IRoleRepository roleRepository;
    private final ITeamRepository teamRepository;
    private final IPlayerRepository playerRepository;

    public InizialiteData(IRoleRepository roleRepository,
                          ITeamRepository teamRepository,
                          IPlayerRepository playerRepository) {

        this.roleRepository = roleRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public void run(String... args) {

        try {
            logger.info("Iniciando insercion de datos iniciales...");

            crearRoleSiNoExiste(RoleType.ADMIN);
            crearRoleSiNoExiste(RoleType.USER);

            crearEquipoConJugadoresSiNoExiste(
                    "FNATIC",
                    "EU",
                    1,
                    new String[][]{
                            {"Boaster", "Jake Howlett", "IGL", "United Kingdom"},
                            {"Alfajer", "Emir Ali Beder", "Sentinel", "Turkey"},
                            {"Chronicle", "Timofey Khromov", "Flex", "Russia"},
                            {"crashies", "Austin Roberts", "Initiator", "United States"},
                            {"kaajak", "Kajetan Haremski", "Duelist", "Poland"}
                    }
            );

            crearEquipoConJugadoresSiNoExiste(
                    "Sentinels",
                    "NA",
                    2,
                    new String[][]{
                            {"johnqt", "Mohamed Amine Ouarid", "IGL", "Morocco"},
                            {"zekken", "Zachary Patrone", "Duelist", "United States"},
                            {"TenZ", "Tyson Ngo", "Flex", "Canada"},
                            {"Sacy", "Gustavo Rossi", "Initiator", "Brazil"},
                            {"Zellsis", "Jordan Montemurro", "Sentinel", "United States"}
                    }
            );

            crearEquipoConJugadoresSiNoExiste(
                    "Paper Rex",
                    "APAC",
                    3,
                    new String[][]{
                            {"f0rsakeN", "Jason Susanto", "Flex", "Indonesia"},
                            {"something", "Ilya Petrov", "Duelist", "Russia"},
                            {"mindfreak", "Aaron Leonhart", "Controller", "Indonesia"},
                            {"d4v41", "Khalish Rusyaidee", "Initiator", "Malaysia"},
                            {"Jinggg", "Wang Jing Jie", "Duelist", "Singapore"}
                    }
            );

            

            logger.info("Insercion de datos iniciales finalizada");

        } catch (Exception e) {

            logger.error("Error en inicializacion de datos: {}", e.getMessage(), e);

        }
    }

    private void crearRoleSiNoExiste(RoleType rol) {

        if (!roleRepository.existsByRol(rol)) {

            roleRepository.save(new Role(null, rol));

            logger.info("Role añadido: {}", rol);
        }
    }

    private void crearEquipoConJugadoresSiNoExiste(String name,
                                                   String region,
                                                   Integer ranking,
                                                   String[][] jugadores) {

        Team equipoExistente = buscarEquipoPorNombre(name);

        if (equipoExistente != null) {
            logger.info("El equipo {} ya existe. No se inserta de nuevo.", name);
            return;
        }

        Team team = new Team();

        team.setName(name);
        team.setRegion(region);
        team.setRanking(ranking);
        team.setCreatedAt(LocalDateTime.now());
        team.setPlayers(new ArrayList<>());

        Team savedTeam = teamRepository.save(team);

        logger.info("Equipo añadido: {}", savedTeam.getName());

        for (String[] jugador : jugadores) {

            crearJugadorSiNoExiste(
                    jugador[0],
                    jugador[1],
                    jugador[2],
                    jugador[3],
                    savedTeam
            );
        }
    }

    private void crearJugadorSiNoExiste(String nickname,
                                        String name,
                                        String role,
                                        String country,
                                        Team team) {

        if (existeJugadorPorNickname(nickname)) {
            logger.info("El jugador {} ya existe. No se inserta de nuevo.", nickname);
            return;
        }

        Player player = new Player();

        player.setNickname(nickname);
        player.setName(name);
        player.setRole(role);
        player.setCountry(country);
        player.setCreatedAt(LocalDateTime.now());
        player.setTeam(team);

        playerRepository.save(player);

        logger.info("Jugador añadido: {} al equipo {}", nickname, team.getName());
    }

    private Team buscarEquipoPorNombre(String name) {

        for (Team team : teamRepository.findAll()) {

            if (team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }

        return null;
    }

    private boolean existeJugadorPorNickname(String nickname) {

        for (Player player : playerRepository.findAll()) {

            if (player.getNickname().equalsIgnoreCase(nickname)) {
                return true;
            }
        }

        return false;
    }
}