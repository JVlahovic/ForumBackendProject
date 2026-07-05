package com.january0001.project.forumbackend.security.service;

import com.january0001.project.forumbackend.entity.User;
import com.january0001.project.forumbackend.repository.UserRepository;
import com.january0001.project.forumbackend.security.dto.LoginRequestDTO;
import com.january0001.project.forumbackend.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(LoginRequestDTO loginRequestDTO) {

        User user = userRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> {
                    log.error("Username not found, nothing in DB.");
                    return new IllegalArgumentException("Username not found, please try again.");
                });

        if (!user.getIsActive()) {
            log.error("Login failure, this user was a cunt and got banned.");
            throw new IllegalArgumentException("You have broken the rules and have subsequently been banned. You are no longer allowed to participate in the forum.");
        }

        if (!user.getEmailIsVerified()) {
            log.error("Login failure, this user has not verified their email yet. Verification email resend recommended to user");
            throw new IllegalArgumentException("Please try verifying your email via the registration portal, then try logging in again.");
        }

        //.matches method will check whether the password hashes match
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPasswordHash())) {
            log.error("Incorrect password provided here.");
            throw new IllegalArgumentException("The password provided is not correct. Please try again.");
        }

        log.info("User successfully logged in, commencing token test.");
        return jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().getRoleDescription());
    }
}
