package com.january0001.project.forumbackend.repository;

import com.january0001.project.forumbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByIsDefault(Boolean isDefault);

}
