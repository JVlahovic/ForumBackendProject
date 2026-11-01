package com.january0001.project.forumbackend.controller;

import com.january0001.project.forumbackend.dto.query.ThreadGetDTO;
import com.january0001.project.forumbackend.dto.query.UserProfileGetDTO;
import com.january0001.project.forumbackend.security.component.SecurityUtil;
import com.january0001.project.forumbackend.security.dto.ChangePasswordDTO;
import com.january0001.project.forumbackend.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final SecurityUtil securityUtil;

    @GetMapping("/me")
    public ResponseEntity<UserProfileGetDTO> getMe(Authentication authentication) {
        securityUtil.requireAuthentication(authentication);
        return new ResponseEntity<>(userService.getProfile(authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/me/threads")
    public ResponseEntity<List<ThreadGetDTO>> getMyThreads(Authentication authentication) {
        securityUtil.requireAuthentication(authentication);
        return new ResponseEntity<>(userService.getThreads(authentication.getName()), HttpStatus.OK);
    }

    @PutMapping("/me/password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO dto,
                                                 Authentication authentication) {
        securityUtil.requireAuthentication(authentication);
        userService.changePassword(authentication.getName(), dto);
        return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
    }

}
