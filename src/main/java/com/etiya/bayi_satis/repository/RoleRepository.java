package com.etiya.bayi_satis.repository;

import com.etiya.bayi_satis.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRolename(String name);
}