package com.january0001.project.forumbackend.dto.query;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostGetDTO {

    private Integer id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean isEdited;

    private Integer authorId;
    private Integer threadId;

}
