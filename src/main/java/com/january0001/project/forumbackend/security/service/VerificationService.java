package com.january0001.project.forumbackend.security.service;

import com.january0001.project.forumbackend.entity.User;
import com.january0001.project.forumbackend.repository.UserRepository;
import com.january0001.project.forumbackend.security.dto.ResendVerificationDTO;
import com.january0001.project.forumbackend.security.dto.VerifyRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationService {

    private final UserRepository userRepository;
    private final EmailService emailService;


    public void verify(@Valid VerifyRequestDTO verifyRequestDTO) {

        User user = userRepository.findByEmail(verifyRequestDTO.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with specified email does not exist."));
        if (user.getEmailIsVerified() == true) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already verified.");
        }

        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(verifyRequestDTO.getVerificationCode())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Mismatch detected in verification code!");
        }

        if (LocalDateTime.now().isAfter(user.getVerificationCodeExpiry())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Verification code expired! Please try the resend function!");
        }

        user.setEmailIsVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
        userRepository.save(user);

    }

    public void resend (ResendVerificationDTO resendVerificationDTO) {
        User user = userRepository.findByEmail(resendVerificationDTO.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with specified email does not exist"));
        if (user.getEmailIsVerified() == true) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already verified");
        }

        String verificationCode = String.format("%04d", new Random().nextInt(9999));
        user.setVerificationCode(verificationCode);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        emailService.sendVerificationCode(user.getEmail(), verificationCode);
    }
}
