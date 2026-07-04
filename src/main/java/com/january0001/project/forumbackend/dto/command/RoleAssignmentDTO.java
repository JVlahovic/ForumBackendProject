package com.january0001.project.forumbackend.dto.command;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoleAssignmentDTO {

    @NotNull
    private Integer roleId;

}
