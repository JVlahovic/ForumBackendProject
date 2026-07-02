package com.january0001.project.forumbackend.controller;

import com.january0001.project.forumbackend.dto.command.ThreadPostDTO;
import com.january0001.project.forumbackend.dto.query.ThreadGetDTO;
import com.january0001.project.forumbackend.security.component.SecurityUtil;
import com.january0001.project.forumbackend.service.ThreadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/threads")
@RequiredArgsConstructor
@Slf4j
public class ThreadController {

    private final ThreadService threadService;
    private final SecurityUtil securityUtil;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ThreadGetDTO>> getThreadsByCategory(@PathVariable Integer categoryId, Authentication authentication) {

        String accessCtrl = threadService.getCategoryAccessCtrl(categoryId);

        if(!securityUtil.hasCategoryAccess(accessCtrl, authentication)) {
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required to view this category. (Registered users only)");
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission to view category is denied.");
        }

        List<ThreadGetDTO> response = threadService.getThreadsForCategory(categoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ThreadGetDTO> createThread(@Valid @RequestBody ThreadPostDTO threadPostDTO, Authentication authentication) {

        String accessCtrl = threadService.getCategoryAccessCtrl(threadPostDTO.getCategoryId());
        String postCtrl = threadService.getCategoryPostCtrl(threadPostDTO.getCategoryId());

        if (!securityUtil.hasCategoryAccess(accessCtrl, authentication)) {
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required to view this. (Registered users only).");
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission to view here, only dabmins.");
        }

        if (!securityUtil.hasPostAccess(postCtrl, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to post here.");
        }

        if (threadPostDTO.getIsPinned() || threadPostDTO.getIsLocked()) {
            if (!securityUtil.hasModOrAdminAuthority(authentication)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to perform this action.");
            }
        }

        ThreadGetDTO response = threadService.createThread(threadPostDTO, authentication);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{threadId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ThreadGetDTO> deleteThread(@PathVariable Integer threadId) {
        threadService.deleteThread(threadId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }









}
