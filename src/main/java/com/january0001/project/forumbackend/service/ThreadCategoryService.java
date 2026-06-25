package com.january0001.project.forumbackend.service;

import com.january0001.project.forumbackend.dto.query.ThreadCategoryGetDTO;
import com.january0001.project.forumbackend.entity.ThreadCategory;
import com.january0001.project.forumbackend.mapper.ThreadCategoryMapper;
import com.january0001.project.forumbackend.repository.ThreadCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThreadCategoryService {

    private final ThreadCategoryRepository threadCategoryRepository;
    private final ThreadCategoryMapper threadCategoryMapper;

    public List<ThreadCategoryGetDTO> getAllCategories() {
        List<ThreadCategory> threadCategories = threadCategoryRepository.findAll();
        return threadCategoryMapper.toDtoList(threadCategories);
    }


}
