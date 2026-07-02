package com.january0001.project.forumbackend.dto.gate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ThreadGateDTO {

    //I made this so I don't have to write lengthy multi checks for some methods in Post.
    private String accessCtrl;
    private String postCtrl;
    private Boolean isLocked;

}
