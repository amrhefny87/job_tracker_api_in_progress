package com.amrhefny.jobtracker.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRespository extends JpaRepository<Role, Long> {
    boolean existsByRole(String role);
}
