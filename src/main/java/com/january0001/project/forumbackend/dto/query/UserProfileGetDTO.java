package com.january0001.project.forumbackend.dto.query;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileGetDTO {

    private Integer id;
    private String username;
    private LocalDateTime registrationDate;
    private String roleDescription;

}
