package com.january0001.project.forumbackend.controller;

import com.january0001.project.forumbackend.dto.command.RoleAssignmentDTO;
import com.january0001.project.forumbackend.dto.query.AdminUserGetDTO;
import com.january0001.project.forumbackend.dto.query.RoleGetDTO;
import com.january0001.project.forumbackend.dto.query.ThreadGetDTO;
import com.january0001.project.forumbackend.filter.ThreadFilter;
import com.january0001.project.forumbackend.filter.UserFilter;
import com.january0001.project.forumbackend.service.GAService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminUserGetDTO>> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer roleId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            Authentication authentication) {
        UserFilter filter = new UserFilter(username, roleId, authentication.getName());
        return new ResponseEntity<>(gaService.getUsers(filter, pageable), HttpStatus.OK);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleGetDTO>> getRoles() {
        return new ResponseEntity<>(gaService.getRoles(), HttpStatus.OK);
    }



}
