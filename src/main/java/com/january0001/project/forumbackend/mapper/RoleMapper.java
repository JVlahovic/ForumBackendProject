package com.january0001.project.forumbackend.mapper;

import com.january0001.project.forumbackend.dto.query.RoleGetDTO;
import com.january0001.project.forumbackend.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleGetDTO toDto(Role role);
    List<RoleGetDTO> toDtoList(List<Role> roles);

}
