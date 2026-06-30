package com.january0001.project.forumbackend.service;

import com.january0001.project.forumbackend.repository.ThreadCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class ForumPostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private ThreadCategoryRepository threadCategoryRepository;

}