package com.january0001.project.forumbackend.service;

import com.january0001.project.forumbackend.entity.ThreadCategory;
import com.january0001.project.forumbackend.repository.ThreadCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class ForumPostServiceTest {

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ThreadCategoryRepository threadCategoryRepository;

}