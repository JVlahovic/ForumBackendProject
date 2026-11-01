package com.january0001.project.forumbackend.security.service;

import com.january0001.project.forumbackend.dto.query.ThreadGetDTO;
import com.january0001.project.forumbackend.dto.query.UserProfileGetDTO;
import com.january0001.project.forumbackend.entity.User;
import com.january0001.project.forumbackend.mapper.ThreadMapper;
import com.january0001.project.forumbackend.mapper.UserProfileMapper;
import com.january0001.project.forumbackend.repository.ThreadRepository;
import com.january0001.project.forumbackend.repository.UserRepository;
import com.january0001.project.forumbackend.security.dto.ChangePasswordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ThreadRepository threadRepository;
    private final UserProfileMapper userProfileMapper;
    private final ThreadMapper threadMapper;
    private final PasswordEncoder passwordEncoder;

    public UserProfileGetDTO getProfile(String username) {
        User user = userRepository.findByUsernameWithRole(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userProfileMapper.toDto(user);
    }

    public List<ThreadGetDTO> getThreads(String username) {
        return threadMapper.toDtoList(threadRepository.findByAuthorUsernameWithRefs(username));
    }

    public void changePassword(String username, ChangePasswordDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect.");
        }

        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }


}
