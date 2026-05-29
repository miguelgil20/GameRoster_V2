package com.backend.gameroster.services.player;

import com.backend.gameroster.dto.player.PlayerCreateDTO;
import com.backend.gameroster.dto.player.PlayerDTO;

import java.util.List;

public interface IPlayerService {

    List<PlayerDTO> findAll();

    PlayerDTO findById(Long id);

    PlayerDTO save(PlayerCreateDTO playerCreateDTO);

    PlayerDTO update(Long id, PlayerCreateDTO playerCreateDTO);

    void deleteById(Long id);
}