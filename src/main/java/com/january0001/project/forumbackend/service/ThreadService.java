package com.january0001.project.forumbackend.service;

import com.january0001.project.forumbackend.dto.query.ThreadGetDTO;
import com.january0001.project.forumbackend.entity.ThreadCategory;
import com.january0001.project.forumbackend.mapper.ThreadMapper;
import com.january0001.project.forumbackend.repository.ThreadCategoryRepository;
import com.january0001.project.forumbackend.repository.ThreadRepository;
import com.january0001.project.forumbackend.security.component.SecurityUtil;
import com.january0001.project.forumbackend.entity.Thread;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThreadService {

    private final ThreadCategoryRepository threadCategoryRepository;
    private final ThreadRepository threadRepository;
    private final ThreadMapper threadMapper;
    private final SecurityUtil securityUtil;

    public List<ThreadGetDTO> getThreadsForCategory(Integer categoryId, Authentication authentication) {

        //Because threads are inside categories, I need to first make sure the applicable category exists.
        //Might help me rule out odd cases later if the funny dementia strikes again.
        ThreadCategory category = threadCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category not found"));

        String accessControl = category.getAccessCtrl();

        if(accessControl.equals("view:registered")) {
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only members can view this thread.");
            }
        }
        else if (accessControl.equals("view:moderator")) {
            if (authentication == null || !securityUtil.hasModOrAdminAuthority(authentication)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only staff can view this thread.");
            }
        }

        List<Thread> threadList = threadRepository.findByThreadCategory(category);
        return threadMapper.toDtoList(threadList);
    }
}
