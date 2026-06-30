package com.january0001.project.forumbackend.controller;

import com.january0001.project.forumbackend.dto.command.ThreadCategoryPostDTO;
import com.january0001.project.forumbackend.dto.query.ThreadCategoryGetDTO;
import com.january0001.project.forumbackend.security.component.SecurityUtil;
import com.january0001.project.forumbackend.service.ThreadCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/thread-categories")
@RequiredArgsConstructor
@Slf4j
public class ThreadCategoryController {

    private final ThreadCategoryService threadCategoryService;
    private final SecurityUtil securityUtil;

    //a bit more of a complicated controller but handles the auth automatically.
    @GetMapping
    ResponseEntity<List<ThreadCategoryGetDTO>> getAllThreadCategories(Authentication authentication) {
        List<ThreadCategoryGetDTO> response;

        if (authentication == null || !authentication.isAuthenticated()) {
            response = threadCategoryService.getGuestCategories();
        }
        else if (securityUtil.hasModOrAdminAuthority(authentication)) {
            response = threadCategoryService.getAllCategories();
        }
        else {
            response = threadCategoryService.getRegisteredCategories();
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ThreadCategoryGetDTO> createCategory(@RequestBody ThreadCategoryPostDTO postDTO) {
        ThreadCategoryGetDTO newCategory = threadCategoryService.createCategory(postDTO);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        threadCategoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
