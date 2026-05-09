package com.january0001.project.forumbackend.controller;

import com.january0001.project.forumbackend.service.ForumPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
@Slf4j
public class ForumPostController {

    private final ForumPostService forumPostService;

}
