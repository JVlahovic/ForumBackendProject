package com.january0001.project.forumbackend.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyRequestDTO {

    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Verification code is required here.")
    @Size(min = 4, max = 4, message = "Verification code must be exactly 4 digits.")
    private String verificationCode;

}
