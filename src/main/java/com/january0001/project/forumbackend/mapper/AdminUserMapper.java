package com.january0001.project.forumbackend.mapper;

import com.january0001.project.forumbackend.dto.query.AdminUserGetDTO;
import com.january0001.project.forumbackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminUserMapper {

    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.roleDescription", target = "roleDescription")
    AdminUserGetDTO toDto(User user);

}
