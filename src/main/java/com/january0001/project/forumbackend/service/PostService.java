package com.january0001.project.forumbackend.service;

import com.january0001.project.forumbackend.dto.command.PostPostDTO;
import com.january0001.project.forumbackend.dto.gate.ThreadGateDTO;
import com.january0001.project.forumbackend.dto.query.PostGetDTO;
import com.january0001.project.forumbackend.entity.Post;
import com.january0001.project.forumbackend.entity.ThreadCategory;
import com.january0001.project.forumbackend.entity.User;
import com.january0001.project.forumbackend.mapper.PostMapper;
import com.january0001.project.forumbackend.repository.PostRepository;
import com.january0001.project.forumbackend.entity.Thread;

import com.january0001.project.forumbackend.repository.ThreadRepository;
import com.january0001.project.forumbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ThreadRepository threadRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public Page<PostGetDTO> getPostsByThreadId(Integer threadId, Pageable pageable) {
        Thread thread =  threadRepository.findById(threadId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found"));
        Page<Post> postPage = postRepository.findByThread(thread, pageable);
        return postPage.map(postMapper::toDto);
    }

    public String getHostCategoryAccessCtrl(Integer threadId) {
        Thread thread = threadRepository.findById(threadId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No thread found"));
        return thread.getThreadCategory().getAccessCtrl();
    }

    public ThreadGateDTO getThreadGateDataDTO(Integer threadId) {
         Thread thread = threadRepository.findById(threadId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found"));
         ThreadCategory category = thread.getThreadCategory();
         return new ThreadGateDTO(category.getAccessCtrl(), category.getPostCtrl(), thread.getIsLocked());
    }

    public PostGetDTO createPost(PostPostDTO postPostDTO, Integer threadId, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Thread thread =  threadRepository.findById(threadId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found"));

        Post post = new Post();
        post.setContent(postPostDTO.getContent());
        post.setAuthor(user);
        post.setThread(thread);
        post.setCreatedAt(LocalDateTime.now());
        post.setIsEdited(false);
        return postMapper.toDto(postRepository.save(post));
    }
}
