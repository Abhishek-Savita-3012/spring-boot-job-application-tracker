package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.*;
import com.abhishek.jobtracker.entity.ApplicationStatus;
import com.abhishek.jobtracker.entity.EmploymentType;
import com.abhishek.jobtracker.entity.WorkMode;
import com.abhishek.jobtracker.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.abhishek.jobtracker.dto.PageResponse;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobApplicationResponse>> createApplication(@Valid @RequestBody CreateJobApplicationRequest request) {

        JobApplicationResponse response = jobApplicationService.createApplication(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Job application created successfully",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<JobApplicationResponse>>> getApplications(

            @RequestParam(required = false)
            String keyword,

            @RequestParam(required = false)
            ApplicationStatus status,

            @RequestParam(required = false)
            WorkMode workMode,

            @RequestParam(required = false)
            EmploymentType employmentType,

            @RequestParam(defaultValue = "false")
            boolean archived,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "createdAt")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction
    ) {

        PageResponse<JobApplicationResponse> response = jobApplicationService.getApplications(
                keyword,
                status,
                workMode,
                employmentType,
                archived,
                page,
                size,
                sortBy,
                direction
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Applications fetched successfully",
                        response
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> getApplicationById(@PathVariable Long id) {

        JobApplicationResponse response = jobApplicationService.getApplicationById(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Job application fetched successfully",
                        response
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> updateApplication(@PathVariable Long id, @Valid @RequestBody UpdateJobApplicationRequest request) {

        JobApplicationResponse response = jobApplicationService.updateApplication(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Job application updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {

        jobApplicationService.deleteApplication(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {

        JobApplicationResponse response = jobApplicationService.updateStatus(id, request.getStatus());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Application status updated successfully",
                        response
                )
        );
    }
    @GetMapping("/{id}/status-history")
    public ResponseEntity<ApiResponse<List<StatusHistoryResponse>>> getStatusHistory(@PathVariable Long id) {

        List<StatusHistoryResponse> history = jobApplicationService.getStatusHistory(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Status history fetched successfully",
                        history
                )
        );
    }

    @PatchMapping("/{id}/resume/{resumeId}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> attachResume(@PathVariable Long id, @PathVariable Long resumeId) {

        JobApplicationResponse response = jobApplicationService.attachResume(id, resumeId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Resume attached successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}/resume")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> detachResume(@PathVariable Long id) {

        JobApplicationResponse response = jobApplicationService.detachResume(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Resume detached successfully",
                        response
                )
        );
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> archiveApplication(@PathVariable Long id) {

        JobApplicationResponse response = jobApplicationService.archiveApplication(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Job application archived successfully",
                        response
                )
        );
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> restoreApplication(@PathVariable Long id) {

        JobApplicationResponse response = jobApplicationService.restoreApplication(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Job application restored successfully",
                        response
                )
        );
    }
}