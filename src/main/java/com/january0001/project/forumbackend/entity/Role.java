package com.january0001.project.forumbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.january0001.project.forumbackend.converter.PermissionConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.january0001.project.forumbackend.model.Permissions;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "role")

public class Role {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "role_code", nullable = false)
    private Integer roleCode;

    @Column(name = "role_description", length = 128)
    private String roleDescription;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "permissions", columnDefinition = "JSON")
    @Convert(converter = PermissionConverter.class)
    private Permissions permissions;
    //TODO (done this): permissions mapping. We are using a JSON, and as such, it must be specially done.
    //TODO: STEP 2: create baseline permission stacks for admin and user

    @ManyToMany(mappedBy = "roleSet")
    private Set<User> userSet = new HashSet<>();

}
