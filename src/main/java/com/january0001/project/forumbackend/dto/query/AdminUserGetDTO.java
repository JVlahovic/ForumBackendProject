package com.january0001.project.forumbackend.dto.query;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserGetDTO {

    private Integer id;
    private String username;
    private String email;
    private Boolean isActive;
    private Boolean emailIsVerified;
    private LocalDateTime registrationDate;

    private Integer roleId;
    private String roleDescription;

}
