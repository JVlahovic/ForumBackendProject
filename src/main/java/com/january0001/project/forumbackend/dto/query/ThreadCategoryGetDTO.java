package com.january0001.project.forumbackend.dto.query;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ThreadCategoryGetDTO {

    private Integer Id;
    private String name;
    private String description;
    private Integer position;

}
