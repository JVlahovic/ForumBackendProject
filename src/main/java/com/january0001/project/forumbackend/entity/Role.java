package com.january0001.project.forumbackend.entity;

import com.january0001.project.forumbackend.converter.PermissionConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.january0001.project.forumbackend.security.util.Permissions;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.ArrayList;
import java.util.List;

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

    @ToStringExclude
    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST)
    private List<User> userList = new ArrayList<>();


}
