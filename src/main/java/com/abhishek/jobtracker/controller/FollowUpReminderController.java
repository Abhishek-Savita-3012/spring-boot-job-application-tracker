package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.ApiResponse;
import com.abhishek.jobtracker.dto.FollowUpReminderRequest;
import com.abhishek.jobtracker.dto.FollowUpReminderResponse;
import com.abhishek.jobtracker.service.FollowUpReminderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Reminders",
        description = "Manage job application follow-up reminders"
)
@RestController
@RequestMapping("/api/applications/{applicationId}/reminders")
public class FollowUpReminderController {

    private final FollowUpReminderService reminderService;

    public FollowUpReminderController(FollowUpReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FollowUpReminderResponse>> createReminder(@PathVariable Long applicationId, @Valid @RequestBody FollowUpReminderRequest request) {

        FollowUpReminderResponse response = reminderService.createReminder(applicationId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "A reminder created successfully",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FollowUpReminderResponse>>> getAllReminders(@PathVariable Long applicationId) {

        List<FollowUpReminderResponse> response = reminderService.getAllReminders(applicationId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Reminders fetched successfully",
                        response
                )
        );
    }

    @GetMapping("/{reminderId}")
    public ResponseEntity<ApiResponse<FollowUpReminderResponse>> getReminder(@PathVariable Long applicationId, @PathVariable Long reminderId) {

        FollowUpReminderResponse response = reminderService.getReminder(applicationId, reminderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Reminder fetched successfully",
                        response
                )
        );
    }

    @PutMapping("/{reminderId}")
    public ResponseEntity<ApiResponse<FollowUpReminderResponse>> updateReminder(@PathVariable Long applicationId, @PathVariable Long reminderId, @Valid @RequestBody FollowUpReminderRequest request) {

        FollowUpReminderResponse response = reminderService.updateReminder(applicationId, reminderId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Reminder updated successfully",
                        response
                )
        );
    }

    @PatchMapping("/{reminderId}/complete")
    public ResponseEntity<ApiResponse<FollowUpReminderResponse>> completeReminder(@PathVariable Long applicationId, @PathVariable Long reminderId) {

        FollowUpReminderResponse response = reminderService.completeReminder(applicationId, reminderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Reminder marked completed successfully",
                        response
                )
        );
    }

    @PatchMapping("/{reminderId}/cancel")
    public ResponseEntity<ApiResponse<FollowUpReminderResponse>> cancelReminder(@PathVariable Long applicationId, @PathVariable Long reminderId) {

        FollowUpReminderResponse response = reminderService.cancelReminder(applicationId, reminderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Reminder marked cancelled successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{reminderId}")
    public ResponseEntity<Void> deleteReminder(@PathVariable Long applicationId, @PathVariable Long reminderId) {

        reminderService.deleteReminder(applicationId, reminderId);

        return ResponseEntity.noContent().build();
    }
}