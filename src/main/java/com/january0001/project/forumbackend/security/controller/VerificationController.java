package com.january0001.project.forumbackend.security.controller;

import com.january0001.project.forumbackend.security.dto.ResendVerificationDTO;
import com.january0001.project.forumbackend.security.dto.VerifyRequestDTO;
import com.january0001.project.forumbackend.security.service.VerificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emailVerify")
@RequiredArgsConstructor
@Slf4j
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping("/verify")
    ResponseEntity<String> verify(@Valid @RequestBody VerifyRequestDTO verifyRequestDTO) {
        verificationService.verify(verifyRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/resend")
    ResponseEntity<String> resend(@Valid @RequestBody ResendVerificationDTO resendVerificationDTO) {
        verificationService.resend(resendVerificationDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
