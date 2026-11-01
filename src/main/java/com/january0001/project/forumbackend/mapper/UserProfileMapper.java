package com.january0001.project.forumbackend.mapper;

import com.january0001.project.forumbackend.dto.query.UserProfileGetDTO;
import com.january0001.project.forumbackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(source = "role.roleDescription", target = "roleDescription")
    UserProfileGetDTO toDto(User user);

}
