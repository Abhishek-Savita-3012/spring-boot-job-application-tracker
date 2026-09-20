package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.*;
import com.abhishek.jobtracker.entity.ApplicationStatus;
import com.abhishek.jobtracker.entity.EmploymentType;
import com.abhishek.jobtracker.entity.WorkMode;
import com.abhishek.jobtracker.service.JobApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.abhishek.jobtracker.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(
        name = "Job Applications",
        description = "Create, retrieve, update, archive and manage job applications"
)
@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @Operation(
            summary = "Create job application",
            description = """
                Creates a new job application for the
                currently authenticated user.

                The owner is derived from the JWT.
                A user ID is never accepted from the client.
                """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Job application created successfully"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation failed or invalid request"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            )
    })
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

    @Operation(
            summary = "Get job applications",
            description = """
                Returns the authenticated user's job applications.

                Supports:
                - keyword search
                - filtering
                - archived/active selection
                - sorting
                - pagination
                """
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<JobApplicationResponse>>> getApplications(

            @Parameter(
                    description = "Search company, role, location or source",
                    example = "Java"
            )
            @RequestParam(required = false)
            String keyword,

            @Parameter(
                    description = "Filter by application status",
                    example = "INTERVIEW"
            )
            @RequestParam(required = false)
            ApplicationStatus status,

            @Parameter(
                    description = "Filter by work mode",
                    example = "REMOTE"
            )
            @RequestParam(required = false)
            WorkMode workMode,

            @Parameter(
                    description = "Filter by employment type",
                    example = "FULL_TIME"
            )
            @RequestParam(required = false)
            EmploymentType employmentType,

            @Parameter(
                    description = "true returns archived applications; false returns active applications",
                    example = "false"
            )
            @RequestParam(defaultValue = "false")
            boolean archived,

            @Parameter(
                    description = "Zero-based page number",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(
                    description = "Number of records per page. Maximum 100.",
                    example = "10"
            )
            @RequestParam(defaultValue = "10")
            int size,

            @Parameter(
                    description = """
                        Sort field. Supported values:
                        createdAt, updatedAt, company, role,
                        salary, appliedDate, deadline,
                        status, archivedAt
                        """,
                    example = "createdAt"
            )
            @RequestParam(defaultValue = "createdAt")
            String sortBy,

            @Parameter(
                    description = "Sort direction: asc or desc",
                    example = "desc"
            )
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

    @Operation(
            summary = "Get application by ID",
            description = "Returns an application owned by the authenticated user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Application fetched successfully"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Application not found"
            )
    })
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

    @io.swagger.v3.oas.annotations.responses.ApiResponses({

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Job Application Deleted Successfully"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {

        jobApplicationService.deleteApplication(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update application status",
            description = """
                Changes the application's status and automatically
                creates a status-history entry.
                """
    )
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

    @Operation(
            summary = "Archive application",
            description = """
                Archives the application without permanently deleting it.
                Archived applications are excluded from active listings.
                """
    )
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

    @Operation(
            summary = "Restore archived application",
            description = "Restores an archived application to the active application list."
    )
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