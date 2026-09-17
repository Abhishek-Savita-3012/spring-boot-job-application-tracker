package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.ApiResponse;
import com.abhishek.jobtracker.dto.ApplicationNoteRequest;
import com.abhishek.jobtracker.dto.ApplicationNoteResponse;
import com.abhishek.jobtracker.service.ApplicationNoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Application Notes",
        description = "Manage notes belonging to job applications"
)
@RestController
@RequestMapping("/api/applications/{applicationId}/notes")
public class ApplicationNoteController {

    private final ApplicationNoteService applicationNoteService;

    public ApplicationNoteController(ApplicationNoteService applicationNoteService) {
        this.applicationNoteService = applicationNoteService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationNoteResponse>> createNote(@PathVariable Long applicationId, @Valid @RequestBody ApplicationNoteRequest request) {

        ApplicationNoteResponse response = applicationNoteService.createNote(applicationId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Application Note created successfully",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApplicationNoteResponse>>> getAllNotes(@PathVariable Long applicationId) {

        List<ApplicationNoteResponse> response = applicationNoteService.getAllNotes(applicationId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Notes fetched successfully",
                        response
                )
        );
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<ApiResponse<ApplicationNoteResponse>> getNote(@PathVariable Long applicationId, @PathVariable Long noteId) {

        ApplicationNoteResponse response = applicationNoteService.getNote(applicationId, noteId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Application Note fetched successfully",
                        response
                )
        );
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<ApiResponse<ApplicationNoteResponse>> updateNote(@PathVariable Long applicationId, @PathVariable Long noteId, @Valid @RequestBody ApplicationNoteRequest request) {

        ApplicationNoteResponse response = applicationNoteService.updateNote(applicationId, noteId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Application Note updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long applicationId, @PathVariable Long noteId) {

        applicationNoteService.deleteNote(applicationId, noteId);

        return ResponseEntity.noContent().build();
    }
}