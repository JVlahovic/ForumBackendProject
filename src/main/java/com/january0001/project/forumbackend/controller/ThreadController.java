package com.january0001.project.forumbackend.controller;

import com.january0001.project.forumbackend.dto.command.PostPostDTO;
import com.january0001.project.forumbackend.dto.command.ThreadPostDTO;
import com.january0001.project.forumbackend.dto.gate.ThreadGateDTO;
import com.january0001.project.forumbackend.dto.query.PostGetDTO;
import com.january0001.project.forumbackend.dto.query.ThreadGetDTO;
import com.january0001.project.forumbackend.repository.PostRepository;
import com.january0001.project.forumbackend.security.component.SecurityUtil;
import com.january0001.project.forumbackend.service.PostService;
import com.january0001.project.forumbackend.service.ThreadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final PostService postService;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ThreadGetDTO>> getThreadsByCategory(@PathVariable Integer categoryId, Authentication authentication) {

        String accessCtrl = threadService.getCategoryAccessCtrl(categoryId);

        if(!securityUtil.hasCategoryAccess(accessCtrl, authentication)) {

            securityUtil.requireAuthentication(authentication);

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

            securityUtil.requireAuthentication(authentication);

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

    @PatchMapping("/pin/{threadId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ThreadGetDTO> togglePin(@PathVariable Integer threadId) {
        ThreadGetDTO response = threadService.togglePin(threadId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/lock/{threadId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ThreadGetDTO> toggleLock(@PathVariable Integer threadId) {
        ThreadGetDTO response = threadService.toggleLock(threadId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //POSTS GO BELOW, FOR BETTER ROUTING

    @GetMapping("/{threadId}/posts")
    public ResponseEntity<Page<PostGetDTO>> getPostsByThreadId(@PathVariable Integer threadId, @PageableDefault(size = 10) Pageable pageable, Authentication authentication) {

        String accessCtrl = postService.getHostCategoryAccessCtrl(threadId);

        if(!securityUtil.hasCategoryAccess(accessCtrl, authentication)) {

            securityUtil.requireAuthentication(authentication);

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission to view post is denied.");
        }

        Page<PostGetDTO> response = postService.getPostsByThreadId(threadId, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{threadId}/posts")
    public ResponseEntity<PostGetDTO> createPost(@PathVariable Integer threadId, @Valid @RequestBody PostPostDTO postPostDTO, Authentication authentication) {

        securityUtil.requireAuthentication(authentication);

        boolean canPost = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("post:create"));

        if(!canPost) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to post here.");
        }

        ThreadGateDTO gate = postService.getThreadGateDataDTO(threadId);

        if(!securityUtil.hasCategoryAccess(gate.getAccessCtrl(), authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to view or post here.");
        }

        if(!securityUtil.hasPostAccess(gate.getPostCtrl(), authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to post here.");
        }

        if(gate.getIsLocked() && !securityUtil.hasModOrAdminAuthority(authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This thread is locked, and only admins or mods can add new content to it.");
        }

        PostGetDTO response = postService.createPost(postPostDTO, threadId, authentication);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }



}
