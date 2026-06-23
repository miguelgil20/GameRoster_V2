package com.backend.gameroster.controller;

import com.backend.gameroster.dto.player.PlayerCreateDTO;
import com.backend.gameroster.dto.player.PlayerDTO;
import com.backend.gameroster.services.player.IPlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    private IPlayerService playerService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PlayerController(playerService)).build();
    }

    @Test
    void findAll_shouldReturnPlayers() throws Exception {
        when(playerService.findAll()).thenReturn(List.of(playerDTO()));

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nickname").value("TenZ"));
    }

    @Test
    void findById_shouldReturnPlayer() throws Exception {
        when(playerService.findById(1L)).thenReturn(playerDTO());

        mockMvc.perform(get("/api/players/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("TenZ"));
    }

    @Test
    void save_shouldReturnCreatedPlayer() throws Exception {
        when(playerService.save(any(PlayerCreateDTO.class))).thenReturn(playerDTO());

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerCreateDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nickname").value("TenZ"));
    }

    @Test
    void update_shouldReturnUpdatedPlayer() throws Exception {
        when(playerService.update(eq(1L), any(PlayerCreateDTO.class))).thenReturn(playerDTO());

        mockMvc.perform(put("/api/players/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerCreateDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("TenZ"));
    }

    @Test
    void deleteById_shouldReturnNoContent() throws Exception {
        doNothing().when(playerService).deleteById(1L);

        mockMvc.perform(delete("/api/players/1"))
                .andExpect(status().isNoContent());
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
