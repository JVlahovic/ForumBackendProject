package com.january0001.project.forumbackend.repository;

import com.january0001.project.forumbackend.entity.Role;
import com.january0001.project.forumbackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    boolean existsByRole(Role role);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.username = :username")
    Optional<User> findByUsernameWithRole(@Param("username") String username);

    Optional<User> findByEmail(String email);

    List<User> findByEmailIsVerifiedAndVerificationCodeExpiryBefore(Boolean emailIsVerified, LocalDateTime expiry);

    @Query(value = "SELECT u FROM User u JOIN FETCH u.role",
            countQuery = "SELECT COUNT(u) FROM User u")
    Page<User> findAllWithRole(Pageable pageable);

    @Query(value = "SELECT u FROM User u JOIN FETCH u.role WHERE u.username <> :username",
            countQuery = "SELECT COUNT(u) FROM User u WHERE u.username <> :username")
    Page<User> findAllWithRoleExcluding(@Param("username") String username, Pageable pageable);



}
