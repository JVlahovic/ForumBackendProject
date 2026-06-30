package com.january0001.project.forumbackend.dto.query;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ThreadGetDTO {

    private Integer id;
    private String title;
    private Boolean isPinned;
    private Boolean isLocked;
    private LocalDateTime createdAt;

    private Integer authorId;
    private Integer threadCategoryId;

}
