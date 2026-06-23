package com.backend.gameroster.services.player;

import com.backend.gameroster.dto.player.PlayerCreateDTO;
import com.backend.gameroster.dto.player.PlayerDTO;
import com.backend.gameroster.entity.Player;
import com.backend.gameroster.entity.Team;
import com.backend.gameroster.exception.NotFoundEntityException;
import com.backend.gameroster.mappers.IPlayerMapper;
import com.backend.gameroster.repository.player.IPlayerRepository;
import com.backend.gameroster.repository.team.ITeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private IPlayerRepository playerRepository;

    @Mock
    private ITeamRepository teamRepository;

    @Mock
    private IPlayerMapper playerMapper;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void findAll_shouldReturnPlayerDTOList() {
        Player player = player();
        PlayerDTO dto = playerDTO();

        when(playerRepository.findAll()).thenReturn(List.of(player));
        when(playerMapper.toDTOList(List.of(player))).thenReturn(List.of(dto));

        List<PlayerDTO> result = playerService.findAll();

        assertEquals(1, result.size());
        assertEquals("TenZ", result.get(0).getNickname());
    }

    @Test
    void findById_whenPlayerExists_shouldReturnPlayerDTO() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player()));
        when(playerMapper.toDTO(any(Player.class))).thenReturn(playerDTO());

        PlayerDTO result = playerService.findById(1L);

        assertEquals("TenZ", result.getNickname());
    }

    @Test
    void findById_whenPlayerDoesNotExist_shouldThrowNotFound() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> playerService.findById(99L));
    }

    @Test
    void save_whenTeamExists_shouldCreatePlayer() {
        PlayerCreateDTO createDTO = playerCreateDTO();
        Player player = player();
        Team team = team();
        PlayerDTO responseDTO = playerDTO();

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerMapper.toEntity(createDTO)).thenReturn(player);
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toDTO(player)).thenReturn(responseDTO);

        PlayerDTO result = playerService.save(createDTO);

        assertEquals("TenZ", result.getNickname());
        verify(playerRepository).save(player);
    }

    @Test
    void save_whenTeamDoesNotExist_shouldThrowNotFound() {
        PlayerCreateDTO createDTO = playerCreateDTO();
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> playerService.save(createDTO));
    }

    @Test
    void update_whenPlayerAndTeamExist_shouldUpdatePlayer() {
        Player player = player();
        Team team = team();
        PlayerCreateDTO createDTO = playerCreateDTO();

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toDTO(player)).thenReturn(playerDTO());

        PlayerDTO result = playerService.update(1L, createDTO);

        assertEquals("TenZ", result.getNickname());
        verify(playerRepository).save(player);
    }

    @Test
    void deleteById_whenPlayerExists_shouldDeletePlayer() {
        Player player = player();
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        playerService.deleteById(1L);

        verify(playerRepository).delete(player);
    }

    private PlayerCreateDTO playerCreateDTO() {
        return PlayerCreateDTO.builder()
                .nickname("TenZ")
                .name("Tyson Ngo")
                .role("Duelist")
                .country("Canada")
                .teamId(1L)
                .build();
    }

    private Player player() {
        Player player = new Player();
        player.setId(1L);
        player.setNickname("TenZ");
        player.setName("Tyson Ngo");
        player.setRole("Duelist");
        player.setCountry("Canada");
        player.setCreatedAt(LocalDateTime.now());
        player.setTeam(team());
        return player;
    }

    private Team team() {
        Team team = new Team();
        team.setId(1L);
        team.setName("Sentinels");
        team.setRegion("NA");
        team.setRanking(1);
        team.setCreatedAt(LocalDateTime.now());
        return team;
    }

    private PlayerDTO playerDTO() {
        return PlayerDTO.builder()
                .id(1L)
                .nickname("TenZ")
                .name("Tyson Ngo")
                .role("Duelist")
                .country("Canada")
                .createdAt(LocalDateTime.now())
                .teamId(1L)
                .teamName("Sentinels")
                .build();
    }
}
