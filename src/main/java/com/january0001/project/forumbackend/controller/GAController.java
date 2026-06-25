package com.january0001.project.forumbackend.controller;

import com.january0001.project.forumbackend.service.GAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ga")
@RequiredArgsConstructor
@Slf4j
public class GAController {

    private final GAService gaService;

}
