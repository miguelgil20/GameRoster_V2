package com.backend.gameroster.services.favorite;

import com.backend.gameroster.dto.favorite.FavoriteCreateDTO;
import com.backend.gameroster.dto.favorite.FavoriteDTO;

import java.util.List;

public interface IFavoriteService {

    List<FavoriteDTO> findAll();

    FavoriteDTO findById(Long id);

    FavoriteDTO save(FavoriteCreateDTO favoriteCreateDTO);

    FavoriteDTO update(Long id, FavoriteCreateDTO favoriteCreateDTO);

    void deleteById(Long id);

    List<FavoriteDTO> findFavoriteTeamsByUserId(Long userId);
}