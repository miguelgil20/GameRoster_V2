package com.backend.gameroster.controller;

import com.backend.gameroster.dto.favorite.FavoriteCreateDTO;
import com.backend.gameroster.dto.favorite.FavoriteDTO;
import com.backend.gameroster.services.favorite.IFavoriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FavoriteControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private IFavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FavoriteController(favoriteService)).build();
    }

    @Test
    void findAll_shouldReturnFavorites() throws Exception {
        when(favoriteService.findAll()).thenReturn(List.of(favoriteDTO()));

        mockMvc.perform(get("/api/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamName").value("Sentinels"));
    }

    @Test
    void findById_shouldReturnFavorite() throws Exception {
        when(favoriteService.findById(1L)).thenReturn(favoriteDTO());

        mockMvc.perform(get("/api/favorites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName").value("Sentinels"));
    }

    @Test
    void findFavoriteTeamsByUserId_shouldReturnFavorites() throws Exception {
        when(favoriteService.findFavoriteTeamsByUserId(1L)).thenReturn(List.of(favoriteDTO()));

        mockMvc.perform(get("/api/favorites/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamName").value("Sentinels"));
    }

    @Test
    void save_shouldReturnCreatedFavorite() throws Exception {
        when(favoriteService.save(any(FavoriteCreateDTO.class))).thenReturn(favoriteDTO());

        mockMvc.perform(post("/api/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriteCreateDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.teamName").value("Sentinels"));
    }

    @Test
    void update_shouldReturnUpdatedFavorite() throws Exception {
        when(favoriteService.update(eq(1L), any(FavoriteCreateDTO.class))).thenReturn(favoriteDTO());

        mockMvc.perform(put("/api/favorites/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriteCreateDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName").value("Sentinels"));
    }

    @Test
    void deleteById_shouldReturnNoContent() throws Exception {
        doNothing().when(favoriteService).deleteById(1L);

        mockMvc.perform(delete("/api/favorites/1"))
                .andExpect(status().isNoContent());
    }

    private FavoriteCreateDTO favoriteCreateDTO() {
        return FavoriteCreateDTO.builder()
                .userId(1L)
                .teamId(1L)
                .build();
    }

    private FavoriteDTO favoriteDTO() {
        return FavoriteDTO.builder()
                .id(1L)
                .userId(1L)
                .teamId(1L)
                .teamName("Sentinels")
                .region("NA")
                .ranking(1)
                .build();
    }
}
