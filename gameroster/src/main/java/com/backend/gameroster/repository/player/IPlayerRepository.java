package com.backend.gameroster.repository.player;

import com.backend.gameroster.entity.Player;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IPlayerRepository extends CrudRepository<Player, Long> {
}
