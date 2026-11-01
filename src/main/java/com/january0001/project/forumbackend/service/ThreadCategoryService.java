package com.january0001.project.forumbackend.service;

import com.january0001.project.forumbackend.dto.command.ThreadCategoryPostDTO;
import com.january0001.project.forumbackend.dto.query.ThreadCategoryGetDTO;
import com.january0001.project.forumbackend.entity.ThreadCategory;
import com.january0001.project.forumbackend.mapper.ThreadCategoryMapper;
import com.january0001.project.forumbackend.repository.ThreadCategoryRepository;
import com.january0001.project.forumbackend.security.component.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThreadCategoryService {

    private final ThreadCategoryRepository threadCategoryRepository;
    private final ThreadCategoryMapper threadCategoryMapper;
    private final SecurityUtil securityUtil;

    public List<ThreadCategoryGetDTO> getAllCategories(Authentication auth) {
        return toDtoWithCanPost(threadCategoryRepository.findAll(), auth);
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

    public List<ThreadCategoryGetDTO> getGuestCategories(Authentication auth) {
        return toDtoWithCanPost(threadCategoryRepository.findByAccessCtrl("view:everyone"), auth);
    }

    public List<ThreadCategoryGetDTO> getRegisteredCategories(Authentication auth) {
        return toDtoWithCanPost(threadCategoryRepository.findByAccessCtrlIn(List.of("view:everyone", "view:registered")), auth);
    }

    private List<ThreadCategoryGetDTO> toDtoWithCanPost(List<ThreadCategory> cats, Authentication auth) {
        List<ThreadCategoryGetDTO> dtos = threadCategoryMapper.toDtoList(cats);
        for (int i = 0; i < cats.size(); i++) {
            dtos.get(i).setCanPost(securityUtil.hasPostAccess(cats.get(i).getPostCtrl(), auth));
        }
        return dtos;
    }
}
