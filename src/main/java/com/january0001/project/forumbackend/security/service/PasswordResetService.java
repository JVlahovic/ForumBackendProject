package com.january0001.project.forumbackend.security.service;

import com.january0001.project.forumbackend.entity.User;
import com.january0001.project.forumbackend.repository.UserRepository;
import com.january0001.project.forumbackend.security.dto.ResendVerificationDTO;
import com.january0001.project.forumbackend.security.dto.VerifyRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    //Password reset system is based on random password sent to the user's recovery / main email.
    //The reset will be two-stage. First stage is password reset confirmation via a 4 digit verification code. Reusing the verification code field since it is free.
    //The logic first checks if the email is verified, which confirms account exists. (Unverified emails get nuked periodically).
    //Only if the password reset verification succeeds will the actual new password email be sent. This is handled in EmailService.java
    //Secure Random is used here for additional bulletproofing.

    private static final String PASSWORD_CHARS =
            "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public void requestReset(ResendVerificationDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);

        if (user == null || !Boolean.TRUE.equals(user.getEmailIsVerified())) {
            log.info("Password reset requested for unknown or unverified email.");
            return; // generic response upstream prevents enumeration
        }

        String code = String.format("%04d", RANDOM.nextInt(10000));
        user.setVerificationCode(code);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        emailService.sendPasswordResetCode(user.getEmail(), code);
        log.info("Password reset code sent for user '{}'.", user.getUsername());
    }

    public void confirmReset(VerifyRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with specified email does not exist."));

        if (user.getVerificationCode() == null
                || !user.getVerificationCode().equals(dto.getVerificationCode())
                || user.getVerificationCodeExpiry() == null
                || LocalDateTime.now().isAfter(user.getVerificationCodeExpiry())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Invalid or expired code.");
        }

        String newPassword = generatePassword(12);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
        userRepository.save(user);
        emailService.sendNewPassword(user.getEmail(), newPassword);
        log.info("Password reset completed for user '{}'.", user.getUsername());
    }

    //Password gen utility
    private String generatePassword(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(PASSWORD_CHARS.charAt(RANDOM.nextInt(PASSWORD_CHARS.length())));
        }
        return sb.toString();
    }

}