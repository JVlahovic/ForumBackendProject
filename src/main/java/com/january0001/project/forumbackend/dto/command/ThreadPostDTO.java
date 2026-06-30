package com.january0001.project.forumbackend.dto.command;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ThreadPostDTO {

    @NotBlank(message = "Thread title cannot be empty")
    @Size(max = 256, message = "Title cannot exceed 256 characters")
    private String title;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @NotBlank(message = "Initial post content cannot be empty")
    private String content;

    private Boolean isPinned = false;
    private Boolean isLocked = false;

    //prototype here, but mapper will contain things that have to be resolved from security
    //note to self: return here once done with that part...

}
