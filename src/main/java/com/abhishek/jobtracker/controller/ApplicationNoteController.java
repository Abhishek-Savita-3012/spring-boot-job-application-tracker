package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.ApplicationNoteRequest;
import com.abhishek.jobtracker.dto.ApplicationNoteResponse;
import com.abhishek.jobtracker.service.ApplicationNoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications/{applicationId}/notes")
public class ApplicationNoteController {

    private final ApplicationNoteService applicationNoteService;

    public ApplicationNoteController(ApplicationNoteService applicationNoteService) {
        this.applicationNoteService = applicationNoteService;
    }

    @PostMapping
    public ResponseEntity<ApplicationNoteResponse> createNote(@PathVariable Long applicationId, @Valid @RequestBody ApplicationNoteRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        applicationNoteService.createNote(applicationId, request)
                );
    }

    @GetMapping
    public ResponseEntity<List<ApplicationNoteResponse>> getAllNotes(@PathVariable Long applicationId) {

        return ResponseEntity.ok(
                applicationNoteService.getAllNotes(applicationId)
        );
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<ApplicationNoteResponse> getNote(@PathVariable Long applicationId, @PathVariable Long noteId) {

        return ResponseEntity.ok(
                applicationNoteService.getNote(applicationId, noteId)
        );
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<ApplicationNoteResponse> updateNote(@PathVariable Long applicationId, @PathVariable Long noteId, @Valid @RequestBody ApplicationNoteRequest request) {

        return ResponseEntity.ok(
                applicationNoteService.updateNote(applicationId, noteId, request)
        );
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long applicationId, @PathVariable Long noteId) {

        applicationNoteService.deleteNote(applicationId, noteId);

        return ResponseEntity.noContent().build();
    }
}