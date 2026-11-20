package com.january0001.project.forumbackend.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFilter {
    private String username;
    private Integer roleId;
    private String excludeUsername;
}
