package com.january0001.project.forumbackend.dto.query;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoleGetDTO {

    private Integer id;
    private Integer roleCode;
    private String roleDescription;

}
