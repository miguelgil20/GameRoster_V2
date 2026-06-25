package com.backend.gameroster.repository.favorite;

import com.backend.gameroster.entity.Favorite;
import com.backend.gameroster.entity.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFavoriteRepository extends CrudRepository<Favorite, Long> {

    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId")
    List<Favorite> findFavoritesByUserId(@Param("userId") Long userId);
}