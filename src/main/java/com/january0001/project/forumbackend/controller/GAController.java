package com.january0001.project.forumbackend.controller;

import com.january0001.project.forumbackend.dto.command.RoleAssignmentDTO;
import com.january0001.project.forumbackend.service.GAService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/adminpanel")
@RequiredArgsConstructor
@Slf4j
public class GAController {

    private final GAService gaService;

    @PatchMapping("/users/{userId}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> banUser(@PathVariable("userId") Integer userId) {
        gaService.banUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/users/{userId}/unban")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unbanUser(@PathVariable("userId") Integer userId) {
        gaService.unbanUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/users/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> setRole(@PathVariable("userId") Integer userId, @Valid @RequestBody RoleAssignmentDTO roleAssignmentDTO) {
        gaService.assignRole(userId, roleAssignmentDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
