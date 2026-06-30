package com.january0001.project.forumbackend.mapper;

import com.january0001.project.forumbackend.dto.command.ThreadCategoryPostDTO;
import com.january0001.project.forumbackend.dto.query.ThreadCategoryGetDTO;
import com.january0001.project.forumbackend.entity.ThreadCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ThreadCategoryMapper {

    ThreadCategory toEntity(ThreadCategoryPostDTO postDTO);

    ThreadCategoryGetDTO toDto(ThreadCategory category);

    List<ThreadCategoryGetDTO> toDtoList(List<ThreadCategory> categories);

}
