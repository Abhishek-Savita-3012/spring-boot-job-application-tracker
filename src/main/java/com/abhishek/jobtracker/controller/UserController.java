package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.*;
import com.abhishek.jobtracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Users",
        description = "User registration, authentication and profile APIs"
)
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account using name, email and password.",
            security = {}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Email already exists"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody RegisterRequest request) {

        UserResponse response = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "User registered successfully",
                                response
                        )
                );
    }

    @Operation(
            summary = "Login user",
            description = "Authenticates a user and returns a JWT access token.",
            security = {}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login successful"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid email or password"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = userService.loginUser(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User logged-in successfully",
                        response
                )
        );
    }

    @Operation(
            summary = "Get current user",
            description = "Returns the profile of the currently authenticated user."
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(Authentication authentication) {

        String email = authentication.getName();
        UserResponse response = userService.getUserByEmail(email);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "It's me",
                        response
                )
        );
    }
}
