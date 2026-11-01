package com.january0001.project.forumbackend.security.controller;

import com.january0001.project.forumbackend.security.dto.ResendVerificationDTO;
import com.january0001.project.forumbackend.security.dto.VerifyRequestDTO;
import com.january0001.project.forumbackend.security.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/passwordReset")
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/request")
    public ResponseEntity<String> request(@Valid @RequestBody ResendVerificationDTO dto) {
        passwordResetService.requestReset(dto);
        return new ResponseEntity<>("Password reset request received. If this account exists in our system, we will send a verification code. Please check the specified email.", HttpStatus.OK);
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirm(@Valid @RequestBody VerifyRequestDTO dto) {
        passwordResetService.confirmReset(dto);
        return new ResponseEntity<>("Password reset request successful. Please check your email for the new temporary password and remember to modify it at your soonest convenience.", HttpStatus.OK);
    }

}
