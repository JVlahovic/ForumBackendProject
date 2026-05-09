package com.january0001.project.forumbackend.service;

import com.january0001.project.forumbackend.repository.PostRepository;
import com.january0001.project.forumbackend.repository.ThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForumPostService {

    private final PostRepository postRepository;
    private final ThreadRepository threadRepository;

}
