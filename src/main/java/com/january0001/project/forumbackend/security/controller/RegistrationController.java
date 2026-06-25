package com.january0001.project.forumbackend.security.controller;


import com.january0001.project.forumbackend.security.dto.RegisterRequestDTO;
import com.january0001.project.forumbackend.security.service.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final RegisterService registerService;

    @PostMapping("")
    ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        log.info("Received the following registration information: {}", registerRequestDTO);
        registerService.register(registerRequestDTO);
        return new ResponseEntity<>("Registration successful!", HttpStatus.CREATED); //Created will give us 201 if good
    }





}
