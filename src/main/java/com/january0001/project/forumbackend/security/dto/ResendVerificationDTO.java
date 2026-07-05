package com.january0001.project.forumbackend.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResendVerificationDTO {

    @NotBlank(message = "Email is required.")
    private String email;

}
