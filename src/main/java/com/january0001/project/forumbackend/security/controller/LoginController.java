package com.january0001.project.forumbackend.security.controller;

import com.january0001.project.forumbackend.security.dto.LoginRequestDTO;
import com.january0001.project.forumbackend.security.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @PostMapping("")
    ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("Checking user login request here for {}", loginRequestDTO.getUsername());
        return new ResponseEntity<>(loginService.login(loginRequestDTO), HttpStatus.OK);
    }

}
