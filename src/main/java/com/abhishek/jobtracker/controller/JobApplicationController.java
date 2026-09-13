package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.CreateJobApplicationRequest;
import com.abhishek.jobtracker.dto.JobApplicationResponse;
import com.abhishek.jobtracker.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    public ResponseEntity<JobApplicationResponse> createApplication(@Valid @RequestBody CreateJobApplicationRequest request, Authentication authentication) {

        String userEmail = authentication.getName();

        JobApplicationResponse response =
                jobApplicationService.createApplication(
                        request,
                        userEmail
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>> getAllApplications(Authentication authentication) {

        String userEmail = authentication.getName();

        List<JobApplicationResponse> applications = jobApplicationService.getAllApplications(userEmail);

        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getApplicationById(@PathVariable Long id, Authentication authentication) {

        String userEmail = authentication.getName();

        JobApplicationResponse response = jobApplicationService.getApplicationById(id, userEmail);

        return ResponseEntity.ok(response);
    }
}