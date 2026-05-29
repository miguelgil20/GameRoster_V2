package com.backend.gameroster.repository.team;

import com.backend.gameroster.entity.Team;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ITeamRepository extends CrudRepository<Team, Long> {
}
