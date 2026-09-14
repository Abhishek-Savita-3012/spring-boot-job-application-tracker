package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.*;
import com.abhishek.jobtracker.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<JobApplicationResponse> createApplication(@Valid @RequestBody CreateJobApplicationRequest request) {

        JobApplicationResponse response = jobApplicationService.createApplication(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>> getAllApplications() {

        return ResponseEntity.ok(
                jobApplicationService.getAllApplications()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getApplicationById(@PathVariable Long id) {

        return ResponseEntity.ok(
                jobApplicationService.getApplicationById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> updateApplication(@PathVariable Long id, @Valid @RequestBody UpdateJobApplicationRequest request) {

        return ResponseEntity.ok(
                jobApplicationService.updateApplication(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {

        jobApplicationService.deleteApplication(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<JobApplicationResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {

        JobApplicationResponse response = jobApplicationService.updateStatus(id, request.getStatus());

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}/status-history")
    public ResponseEntity<List<StatusHistoryResponse>> getStatusHistory(@PathVariable Long id) {

        return ResponseEntity.ok(
                jobApplicationService
                        .getStatusHistory(id)
        );
    }

    @PatchMapping("/{id}/resume/{resumeId}")
    public ResponseEntity<JobApplicationResponse> attachResume(@PathVariable Long id, @PathVariable Long resumeId) {

        return ResponseEntity.ok(
                jobApplicationService.attachResume(id, resumeId)
        );
    }

    @DeleteMapping("/{id}/resume")
    public ResponseEntity<JobApplicationResponse> detachResume(@PathVariable Long id) {

        return ResponseEntity.ok(
                jobApplicationService.detachResume(id)
        );
    }
}