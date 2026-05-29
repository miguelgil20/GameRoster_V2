package com.backend.gameroster.repository.role;

import com.backend.gameroster.entity.Role;

import com.backend.gameroster.enums.RoleType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IRoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByRol(RoleType rol);

    boolean existsByRol(RoleType rol);
}
