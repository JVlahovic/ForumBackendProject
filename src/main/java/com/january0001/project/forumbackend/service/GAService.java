package com.january0001.project.forumbackend.service;

import com.january0001.project.forumbackend.dto.command.RoleAssignmentDTO;
import com.january0001.project.forumbackend.dto.query.AdminUserGetDTO;
import com.january0001.project.forumbackend.dto.query.RoleGetDTO;
import com.january0001.project.forumbackend.entity.Role;
import com.january0001.project.forumbackend.entity.User;
import com.january0001.project.forumbackend.mapper.AdminUserMapper;
import com.january0001.project.forumbackend.mapper.RoleMapper;
import com.january0001.project.forumbackend.repository.RoleRepository;
import com.january0001.project.forumbackend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GAService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AdminUserMapper adminUserMapper;
    private final RoleMapper roleMapper;



    public void banUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setIsActive(false);
        userRepository.save(user);
    }

    public void unbanUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setIsActive(true);
        userRepository.save(user);
    }

    public void assignRole(Integer userId, @Valid RoleAssignmentDTO roleAssignmentDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Role role = roleRepository.findById(roleAssignmentDTO.getRoleId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        user.setRole(role);
        userRepository.save(user);
    }

    public Page<AdminUserGetDTO> getUsers(String currentUsername, Pageable pageable) {
        return userRepository.findAllWithRoleExcluding(currentUsername, pageable).map(adminUserMapper::toDto);
    }

    public List<RoleGetDTO> getRoles() {
        return roleMapper.toDtoList(roleRepository.findAll(Sort.by("id")));
    }

    //Unverified email nuker so users can re-register at a later time, in case email sending fails.
    @Scheduled(fixedRate = 600000)
    public void cleanupExpiredUnverifiedUsers() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(15);
        List<User> expired = userRepository.findByEmailIsVerifiedAndVerificationCodeExpiryBefore(false, threshold);
        userRepository.deleteAll(expired);
        log.info("Nuked {} expired unverified registrations", expired.size());
    }
}
