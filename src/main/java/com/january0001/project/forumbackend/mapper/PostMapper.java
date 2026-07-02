package com.january0001.project.forumbackend.mapper;

import com.january0001.project.forumbackend.dto.query.PostGetDTO;
import com.january0001.project.forumbackend.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "author.id", target = "authorId")
    PostGetDTO toDto(Post post);
    List<PostGetDTO> toDtoList(List<Post> posts);

}
