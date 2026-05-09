package com.january0001.project.forumbackend.service;

import com.january0001.project.forumbackend.repository.RoleRepository;
import com.january0001.project.forumbackend.repository.ThreadCategoryRepository;
import com.january0001.project.forumbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GAService {

    //Intention: place any administrative stuff here like creating roles, assigning users with a certain role, banning, forum post categories for announcements and that stuff.

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ThreadCategoryRepository threadCategoryRepository;

}
