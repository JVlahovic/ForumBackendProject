package com.january0001.project.forumbackend.security.mapper;

import com.january0001.project.forumbackend.entity.User;
import com.january0001.project.forumbackend.security.dto.RegisterRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role" , ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "emailIsVerified", ignore = true)
    User toEntity(RegisterRequestDTO registerDTO);
}
