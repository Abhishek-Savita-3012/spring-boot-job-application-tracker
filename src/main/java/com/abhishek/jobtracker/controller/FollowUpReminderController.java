package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.FollowUpReminderRequest;
import com.abhishek.jobtracker.dto.FollowUpReminderResponse;
import com.abhishek.jobtracker.service.FollowUpReminderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications/{applicationId}/reminders")
public class FollowUpReminderController {

    private final FollowUpReminderService reminderService;

    public FollowUpReminderController(FollowUpReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public ResponseEntity<FollowUpReminderResponse> createReminder(@PathVariable Long applicationId, @Valid @RequestBody FollowUpReminderRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        reminderService.createReminder(applicationId, request)
                );
    }

    @GetMapping
    public ResponseEntity<List<FollowUpReminderResponse>> getAllReminders(@PathVariable Long applicationId) {

        return ResponseEntity.ok(
                reminderService.getAllReminders(applicationId)
        );
    }

    @GetMapping("/{reminderId}")
    public ResponseEntity<FollowUpReminderResponse> getReminder(@PathVariable Long applicationId, @PathVariable Long reminderId) {

        return ResponseEntity.ok(
                reminderService.getReminder(applicationId, reminderId)
        );
    }

    @PutMapping("/{reminderId}")
    public ResponseEntity<FollowUpReminderResponse> updateReminder(@PathVariable Long applicationId, @PathVariable Long reminderId, @Valid @RequestBody FollowUpReminderRequest request) {

        return ResponseEntity.ok(
                reminderService.updateReminder(applicationId, reminderId, request)
        );
    }

    @PatchMapping("/{reminderId}/complete")
    public ResponseEntity<FollowUpReminderResponse> completeReminder(@PathVariable Long applicationId, @PathVariable Long reminderId) {

        return ResponseEntity.ok(
                reminderService.completeReminder(applicationId, reminderId)
        );
    }

    @PatchMapping("/{reminderId}/cancel")
    public ResponseEntity<FollowUpReminderResponse> cancelReminder(@PathVariable Long applicationId, @PathVariable Long reminderId) {

        return ResponseEntity.ok(
                reminderService.cancelReminder(applicationId, reminderId)
        );
    }

    @DeleteMapping("/{reminderId}")
    public ResponseEntity<Void> deleteReminder(@PathVariable Long applicationId, @PathVariable Long reminderId) {

        reminderService.deleteReminder(applicationId, reminderId);

        return ResponseEntity.noContent().build();
    }
}