package com.january0001.project.forumbackend.mapper;

import com.january0001.project.forumbackend.entity.Thread;
import com.january0001.project.forumbackend.dto.query.ThreadGetDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ThreadMapper {

    //I've been trying to solve this error case for hours but turns out that the source fields were not initially visible to mapstruct because java.lang.Thread was imported by default, and not my own.
    //Importing the Thread entity I made fixed the issue and mapstruct can see it fine now.
    @Mapping(source = "threadCategory.id", target = "threadCategoryId")
    @Mapping(source = "author.id", target = "authorId")

    ThreadGetDTO toDto(Thread thread);

    List<ThreadGetDTO> toDtoList(List<Thread> threads);

}
