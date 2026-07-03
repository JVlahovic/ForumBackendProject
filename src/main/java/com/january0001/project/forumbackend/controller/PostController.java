package com.january0001.project.forumbackend.controller;

import com.january0001.project.forumbackend.dto.command.PostPutDTO;
import com.january0001.project.forumbackend.dto.query.PostGetDTO;
import com.january0001.project.forumbackend.security.component.SecurityUtil;
import com.january0001.project.forumbackend.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final SecurityUtil securityUtil;

    @PutMapping("/{postId}")
    public ResponseEntity<PostGetDTO> updatePost(@PathVariable Integer postId, @Valid @RequestBody PostPutDTO postPutDTO, Authentication authentication) {

        securityUtil.requireAuthentication(authentication);

        PostGetDTO response = postService.updatePost(postId, postPutDTO, authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<PostGetDTO> deletePost(@PathVariable Integer postId, Authentication authentication) {

        securityUtil.requireAuthentication(authentication);
        postService.deletePost(postId, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
