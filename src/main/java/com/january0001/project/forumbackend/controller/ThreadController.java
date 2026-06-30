package com.january0001.project.forumbackend.controller;

import com.january0001.project.forumbackend.dto.query.ThreadCategoryGetDTO;
import com.january0001.project.forumbackend.dto.query.ThreadGetDTO;
import com.january0001.project.forumbackend.service.ThreadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/threads")
@RequiredArgsConstructor
@Slf4j
public class ThreadController {

    private final ThreadService threadService;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ThreadGetDTO>> getThreadsByCategory(@PathVariable Integer categoryId, Authentication authentication) {
        List<ThreadGetDTO> response = threadService.getThreadsForCategory(categoryId, authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
