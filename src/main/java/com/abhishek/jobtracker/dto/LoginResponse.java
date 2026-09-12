package com.abhishek.jobtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Long id;
    private String name;
    private String email;
    private String token;
    private String message;
}
