package com.backend.gameroster.services.favorite;

import com.backend.gameroster.dto.favorite.FavoriteCreateDTO;
import com.backend.gameroster.dto.favorite.FavoriteDTO;
import com.backend.gameroster.entity.Favorite;
import com.backend.gameroster.entity.Team;
import com.backend.gameroster.entity.User;
import com.backend.gameroster.exception.NotFoundEntityException;
import com.backend.gameroster.mappers.IFavoriteMapper;
import com.backend.gameroster.repository.favorite.IFavoriteRepository;
import com.backend.gameroster.repository.team.ITeamRepository;
import com.backend.gameroster.repository.user.IUserRepository;
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
class FavoriteServiceTest {

    @Mock
    private IFavoriteRepository favoriteRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ITeamRepository teamRepository;

    @Mock
    private IFavoriteMapper favoriteMapper;

    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    void findAll_shouldReturnFavoriteDTOList() {
        Favorite favorite = favorite();
        FavoriteDTO dto = favoriteDTO();

        when(favoriteRepository.findAll()).thenReturn(List.of(favorite));
        when(favoriteMapper.toDTOList(List.of(favorite))).thenReturn(List.of(dto));

        List<FavoriteDTO> result = favoriteService.findAll();

        assertEquals(1, result.size());
        assertEquals("Sentinels", result.get(0).getTeamName());
    }

    @Test
    void findById_whenFavoriteExists_shouldReturnFavoriteDTO() {
        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(favorite()));
        when(favoriteMapper.toDTO(any(Favorite.class))).thenReturn(favoriteDTO());

        FavoriteDTO result = favoriteService.findById(1L);

        assertEquals("Sentinels", result.getTeamName());
    }

    @Test
    void findById_whenFavoriteDoesNotExist_shouldThrowNotFound() {
        when(favoriteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> favoriteService.findById(99L));
    }

    @Test
    void save_whenUserAndTeamExist_shouldCreateFavorite() {
        FavoriteCreateDTO createDTO = favoriteCreateDTO();
        Favorite saved = favorite();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team()));
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(saved);
        when(favoriteMapper.toDTO(saved)).thenReturn(favoriteDTO());

        FavoriteDTO result = favoriteService.save(createDTO);

        assertEquals("Sentinels", result.getTeamName());
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void save_whenUserDoesNotExist_shouldThrowNotFound() {
        FavoriteCreateDTO createDTO = favoriteCreateDTO();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> favoriteService.save(createDTO));
    }

    @Test
    void update_whenFavoriteUserAndTeamExist_shouldUpdateFavorite() {
        Favorite favorite = favorite();
        FavoriteCreateDTO createDTO = favoriteCreateDTO();

        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(favorite));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team()));
        when(favoriteRepository.save(favorite)).thenReturn(favorite);
        when(favoriteMapper.toDTO(favorite)).thenReturn(favoriteDTO());

        FavoriteDTO result = favoriteService.update(1L, createDTO);

        assertEquals("Sentinels", result.getTeamName());
        verify(favoriteRepository).save(favorite);
    }

    @Test
    void deleteById_whenFavoriteExists_shouldDeleteFavorite() {
        Favorite favorite = favorite();
        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(favorite));

        favoriteService.deleteById(1L);

        verify(favoriteRepository).delete(favorite);
    }

    @Test
    void findFavoriteTeamsByUserId_shouldReturnFavorites() {
        Favorite favorite = favorite();
        FavoriteDTO dto = favoriteDTO();

        when(favoriteRepository.findFavoritesByUserId(1L)).thenReturn(List.of(favorite));
        when(favoriteMapper.toDTOList(List.of(favorite))).thenReturn(List.of(dto));

        List<FavoriteDTO> result = favoriteService.findFavoriteTeamsByUserId(1L);

        assertEquals(1, result.size());
        assertEquals("Sentinels", result.get(0).getTeamName());
    }

    private FavoriteCreateDTO favoriteCreateDTO() {
        return FavoriteCreateDTO.builder()
                .userId(1L)
                .teamId(1L)
                .build();
    }

    private Favorite favorite() {
        Favorite favorite = new Favorite();
        favorite.setId(1L);
        favorite.setUser(user());
        favorite.setTeam(team());
        favorite.setCreatedAt(LocalDateTime.now());
        return favorite;
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

    private User user() {
        User user = new User();
        user.setCode(1L);
        user.setUsername("miguel");
        user.setEmail("miguel@email.com");
        user.setPassword("encoded-password");
        user.setCreationDate(LocalDateTime.now());
        user.setRoles(List.of());
        return user;
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
}
