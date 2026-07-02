package com.january0001.project.forumbackend.dto.command;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ThreadCategoryPostDTO {

    private String name;
    private String description;
    private Integer position;
    private String accessCtrl;
    private String postCtrl;

}
