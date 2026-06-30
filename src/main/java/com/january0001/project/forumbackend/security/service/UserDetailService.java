package com.january0001.project.forumbackend.security.service;

import com.january0001.project.forumbackend.entity.User;
import com.january0001.project.forumbackend.repository.UserRepository;
import com.january0001.project.forumbackend.security.util.UserDetailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user  = userRepository.findByUsernameWithRole(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new UserDetailUtil(user);
    }
}
