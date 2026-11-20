package com.january0001.project.forumbackend.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThreadFilter {
    private String title;
    private List<String> allowedAccessCtrls;
}
