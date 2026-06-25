package com.january0001.project.forumbackend.controller;

import com.january0001.project.forumbackend.dto.query.ThreadCategoryGetDTO;
import com.january0001.project.forumbackend.service.ThreadCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/thread-categories")
@RequiredArgsConstructor
@Slf4j
public class ForumCategoryController {

    private final ThreadCategoryService threadCategoryService;

    @GetMapping
    ResponseEntity<List<ThreadCategoryGetDTO>> getAllThreadCategories() {
        List<ThreadCategoryGetDTO> response = threadCategoryService.getAllCategories();
        if(response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
