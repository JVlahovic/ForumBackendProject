package com.january0001.project.forumbackend.controller;

import com.january0001.project.forumbackend.entity.ThreadCategory;
import com.january0001.project.forumbackend.service.ForumPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
@Slf4j
public class ForumPostController {

    private final ForumPostService forumPostService;





}
