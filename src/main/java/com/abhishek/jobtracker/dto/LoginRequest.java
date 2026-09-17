package com.abhishek.jobtracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(
        description = "User login credentials"
)
@Getter
@Setter
public class LoginRequest {

    @Schema(
            example = "user@example.com"
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    @Schema(
            example = "ExamplePass123",
            format = "password"
    )
    @NotBlank(message = "Password is required")
    private String password;
}
