package com.january0001.project.forumbackend.security.util;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permissions {

    //actual coconut.jpg situation here. If I remove this, the entire backbone of my capabilities checking goes to shit.
    //DO NOT TOUCH.
    private Set<String> capabilities = new HashSet<>();
    //Also: set not list because if I upgrade a user from user to admin, then any overlapping permissions won't appear twice and clog the db json.

}
