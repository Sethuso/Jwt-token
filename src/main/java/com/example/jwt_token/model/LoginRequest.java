package com.example.jwt_token.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "give me the proper email address")
    @NotBlank(message = "please provide the email id")
    private String email;
    private String password;
}
