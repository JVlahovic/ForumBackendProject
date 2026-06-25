package com.january0001.project.forumbackend.security.service;

import com.january0001.project.forumbackend.entity.Role;
import com.january0001.project.forumbackend.entity.User;
import com.january0001.project.forumbackend.repository.RoleRepository;
import com.january0001.project.forumbackend.repository.UserRepository;
import com.january0001.project.forumbackend.security.config.SecurityConfig;
import com.january0001.project.forumbackend.security.dto.RegisterRequestDTO;
import com.january0001.project.forumbackend.security.mapper.UserMapper;
import com.january0001.project.forumbackend.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@RequiredArgsConstructor
@Slf4j
@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void register (RegisterRequestDTO registerDTO) {
        if(!registerDTO.getTermsAndConditions()) {
            log.error("User attempting to register {} has failed to accept the terms and conditions.", registerDTO.getUsername());
            throw new IllegalArgumentException("You must first accept the terms and conditions. Please try again.");
        }

        //Must create a Select by Username query
        if(userRepository.existsByUsername(registerDTO.getUsername())) {
            log.error("Username {} already exists in DB", registerDTO.getUsername());
            throw new IllegalArgumentException("Username already exists. Please try again with a different username.");
        }

        //Must create a Select by Email query
        if(userRepository.existsByEmail(registerDTO.getEmail())) {
            log.error("Email {} already exists in DB", registerDTO.getEmail());
            throw new IllegalArgumentException("Email already exists. Please try again with a different email.");
        }

        Role defaultRole = roleRepository.findByIsDefault(true).orElseThrow(() -> new IllegalStateException("Default System Role is Missing or unreachable. Contact the administrator for fix."));

        User user = userMapper.toEntity(registerDTO);
        String rawPassword = registerDTO.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        user.setPasswordHash(hashedPassword);

        user.setRole(defaultRole);
        user.setIsActive(true);
        user.setEmailIsVerified(true); //MOCK VALUE, ONCE COMPLETED THE VERIFICATION OF EMAIL STEP THIS SHOULD BE REMOVED AND REPLACED WITH A SOLID FALSE UNTIL THE EMAIL IS VERIFIED.

        userRepository.save(user);

        log.info("Successfully registered new user: '{}' with ID: {}", user.getUsername(), user.getId());
    }

}
