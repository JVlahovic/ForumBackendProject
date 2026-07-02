package com.january0001.project.forumbackend.service;

import com.january0001.project.forumbackend.dto.command.ThreadPostDTO;
import com.january0001.project.forumbackend.dto.query.ThreadGetDTO;
import com.january0001.project.forumbackend.entity.Post;
import com.january0001.project.forumbackend.entity.ThreadCategory;
import com.january0001.project.forumbackend.entity.User;
import com.january0001.project.forumbackend.mapper.ThreadMapper;
import com.january0001.project.forumbackend.repository.ThreadCategoryRepository;
import com.january0001.project.forumbackend.repository.ThreadRepository;
import com.january0001.project.forumbackend.repository.UserRepository;
import com.january0001.project.forumbackend.entity.Thread;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThreadService {

    private final ThreadCategoryRepository threadCategoryRepository;
    private final ThreadRepository threadRepository;
    private final ThreadMapper threadMapper;
    private final UserRepository userRepository;

    //Because threads are inside categories, I need to first make sure the applicable category exists.
    //Might help me rule out odd cases later if the funny dementia strikes again.
    //UPDATE: Separate concerns of functions here. More legible.
    public String getCategoryAccessCtrl(Integer categoryId) {
        return threadCategoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")).getAccessCtrl();
    }
    public String getCategoryPostCtrl(Integer categoryId) {
        return threadCategoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")).getPostCtrl();
    }

    public List<ThreadGetDTO> getThreadsForCategory(Integer categoryId) {

        ThreadCategory category = threadCategoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        List<Thread> threadList = threadRepository.findByThreadCategory(category);
        return threadMapper.toDtoList(threadList);

    }

    public ThreadGetDTO createThread(@Valid ThreadPostDTO threadPostDTO, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        ThreadCategory category = threadCategoryRepository.findById(threadPostDTO.getCategoryId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        //Several things here wouldn't work with mapstruct because mapstruct works only with one entity, rather than multiple at a time. I am NOT changing the DB so it can accomodate a tool, so I am manually mapping this here.
        Thread thread = new Thread();
        thread.setTitle(threadPostDTO.getTitle());
        thread.setIsPinned(threadPostDTO.getIsPinned());
        thread.setIsLocked(threadPostDTO.getIsLocked());
        thread.setCreatedAt(LocalDateTime.now());
        thread.setUpdatedAt(LocalDateTime.now());
        thread.setAuthor(user);
        thread.setThreadCategory(category);

        Post initialPost = new Post();
        initialPost.setContent(threadPostDTO.getContent());
        initialPost.setCreatedAt(LocalDateTime.now());
        initialPost.setIsEdited(false);
        initialPost.setAuthor(user);
        initialPost.setThread(thread);

        thread.setPostList(List.of(initialPost));
        Thread savedThread = threadRepository.save(thread);
        return threadMapper.toDto(savedThread);

    }

    public void deleteThread(Integer threadId) {
        Thread thread = threadRepository.findById(threadId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not delete thread: thread not found"));
        threadRepository.delete(thread);
    }

    public ThreadGetDTO togglePin(Integer threadId) {
        Thread thread = threadRepository.findById(threadId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not toggle pin: thread not found"));
        thread.setIsPinned(!thread.getIsPinned());
        return threadMapper.toDto(threadRepository.save(thread));
    }

    public ThreadGetDTO toggleLock(Integer threadId) {
        Thread thread = threadRepository.findById(threadId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not toggle lock: thread not found"));
        thread.setIsLocked(!thread.getIsLocked());
        return threadMapper.toDto(threadRepository.save(thread));
    }
}
