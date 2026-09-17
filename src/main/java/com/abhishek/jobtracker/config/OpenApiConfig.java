package com.abhishek.jobtracker.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration

@OpenAPIDefinition(
        info = @Info(
                title = "Job Application Tracking API",
                version = "1.0",
                description = """
                        Backend REST API for tracking job applications,
                        interview rounds, notes, resumes, reminders,
                        application status history and dashboard analytics.
                        """,
                contact = @Contact(
                        name = "Abhishek Savita"
                )
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication using Bearer token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)

public class OpenApiConfig {
}