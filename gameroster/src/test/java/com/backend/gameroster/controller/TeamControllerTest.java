package com.backend.gameroster.controller;

import com.backend.gameroster.dto.team.TeamDTO;
import com.backend.gameroster.services.team.ITeamService;
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
class TeamControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    private ITeamService teamService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TeamController(teamService)).build();
    }

    @Test
    void findAll_shouldReturnTeams() throws Exception {
        when(teamService.findAll()).thenReturn(List.of(teamDTO()));

        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sentinels"));
    }

    @Test
    void findById_shouldReturnTeam() throws Exception {
        when(teamService.findById(1L)).thenReturn(teamDTO());

        mockMvc.perform(get("/api/teams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sentinels"));
    }

    @Test
    void save_shouldReturnCreatedTeam() throws Exception {
        TeamDTO dto = teamDTO();
        when(teamService.save(any(TeamDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sentinels"));
    }

    @Test
    void update_shouldReturnUpdatedTeam() throws Exception {
        TeamDTO dto = teamDTO();
        when(teamService.update(eq(1L), any(TeamDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sentinels"));
    }

    @Test
    void deleteById_shouldReturnNoContent() throws Exception {
        doNothing().when(teamService).deleteById(1L);

        mockMvc.perform(delete("/api/teams/1"))
                .andExpect(status().isNoContent());
    }

    private TeamDTO teamDTO() {
        return TeamDTO.builder()
                .id(1L)
                .name("Sentinels")
                .region("NA")
                .ranking(1)
                .createdAt(LocalDateTime.now())
                .players(List.of())
                .build();
    }
}
