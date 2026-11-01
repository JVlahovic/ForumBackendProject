package com.january0001.project.forumbackend.security;

import com.january0001.project.forumbackend.entity.Role;
import com.january0001.project.forumbackend.entity.User;
import com.january0001.project.forumbackend.repository.RoleRepository;
import com.january0001.project.forumbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:}") private String adminUsername;
    @Value("${app.admin.password:}") private String adminPassword;
    @Value("${app.admin.email:}")    private String adminEmail;

    @Override
    public void run(String... args) {
        if (adminUsername.isBlank() || adminPassword.isBlank()) {
            log.info("Admin bootstrap skipped (no credentials configured).");
            return;
        }
        Role adminRole = roleRepository.findByRoleDescription("Admin")
                .orElseThrow(() -> new IllegalStateException("Admin role missing"));

        if (userRepository.existsByRole(adminRole)) {
            log.info("An admin already exists; bootstrap skipped.");
            return;
        }

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail.isBlank() ? adminUsername + "@local" : adminEmail);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setRole(adminRole);
        admin.setIsActive(true);
        admin.setEmailIsVerified(true);
        userRepository.save(admin);
        log.info("Bootstrap admin '{}' created.", adminUsername);
    }

}
