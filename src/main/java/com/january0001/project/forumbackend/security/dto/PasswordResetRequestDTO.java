package com.january0001.project.forumbackend.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequestDTO {

    @NotBlank(message = "Email is required.")
    @Email(message = "A valid email is required.")
    private String email;

}
