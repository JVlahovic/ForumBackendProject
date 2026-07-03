package com.january0001.project.forumbackend.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostPutDTO {

    @NotBlank(message = "Reply edit cannot be empty.")
    @Size(max = 4096, message = "Title cannot exceed 4096 characters.")
    private String content;

}
