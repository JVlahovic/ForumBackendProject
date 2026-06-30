package com.january0001.project.forumbackend.service;

import com.january0001.project.forumbackend.dto.command.ThreadCategoryPostDTO;
import com.january0001.project.forumbackend.dto.query.ThreadCategoryGetDTO;
import com.january0001.project.forumbackend.entity.ThreadCategory;
import com.january0001.project.forumbackend.mapper.ThreadCategoryMapper;
import com.january0001.project.forumbackend.repository.ThreadCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
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


    public ThreadCategoryGetDTO createCategory(ThreadCategoryPostDTO postDTO) {
        ThreadCategory threadCategory = threadCategoryMapper.toEntity(postDTO);
        ThreadCategory savedCategory = threadCategoryRepository.save(threadCategory);
        return threadCategoryMapper.toDto(savedCategory);
    }

    public void deleteCategory(Integer id) {

        ThreadCategory category = threadCategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found with ID" + id));

        if (!category.getThreads().isEmpty()){
            throw new IllegalStateException("Deletion aborted: this category still contains threads! Either delete or move the threads manually before attempting this again.");
        }
        threadCategoryRepository.delete(category);
    }

    public List<ThreadCategoryGetDTO> getGuestCategories() {
        List<ThreadCategory> threadCategories = threadCategoryRepository.findByAccessCtrl("view:everyone");
        return threadCategoryMapper.toDtoList(threadCategories);
    }

    public List<ThreadCategoryGetDTO> getRegisteredCategories() {
        List<ThreadCategory> threadCategories = threadCategoryRepository.findByAccessCtrlIn(List.of("view:everyone", "view:registered"));
        return threadCategoryMapper.toDtoList(threadCategories);
    }
}
