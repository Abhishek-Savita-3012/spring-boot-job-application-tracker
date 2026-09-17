package com.abhishek.jobtracker.service;

import com.abhishek.jobtracker.dto.FollowUpReminderRequest;
import com.abhishek.jobtracker.dto.FollowUpReminderResponse;
import com.abhishek.jobtracker.entity.FollowUpReminder;
import com.abhishek.jobtracker.entity.JobApplication;
import com.abhishek.jobtracker.entity.ReminderStatus;
import com.abhishek.jobtracker.entity.User;
import com.abhishek.jobtracker.exception.ApplicationNotFoundException;
import com.abhishek.jobtracker.exception.ResourceNotFoundException;
import com.abhishek.jobtracker.repository.FollowUpReminderRepository;
import com.abhishek.jobtracker.repository.JobApplicationRepository;
import com.abhishek.jobtracker.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowUpReminderService {

    private final FollowUpReminderRepository reminderRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final CurrentUserService currentUserService;

    public FollowUpReminderService(FollowUpReminderRepository reminderRepository, JobApplicationRepository jobApplicationRepository, CurrentUserService currentUserService) {
        this.reminderRepository = reminderRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.currentUserService = currentUserService;
    }

    private JobApplication getOwnedApplication(Long applicationId) {

        User user = currentUserService.getCurrentUser();

        return jobApplicationRepository.findByIdAndUser_Id(applicationId, user.getId())
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Job application not found"
                        )
                );
    }

    public FollowUpReminderResponse createReminder(Long applicationId, FollowUpReminderRequest request) {

        JobApplication application = getOwnedApplication(applicationId);

        FollowUpReminder reminder =
                FollowUpReminder.builder()
                        .remindAt(request.getRemindAt())
                        .message(request.getMessage())
                        .status(ReminderStatus.PENDING)
                        .jobApplication(application)
                        .build();

        FollowUpReminder savedReminder = reminderRepository.save(reminder);

        return mapToResponse(savedReminder);
    }

    public List<FollowUpReminderResponse> getAllReminders(Long applicationId) {

        getOwnedApplication(applicationId);

        return reminderRepository.findAllByJobApplication_IdOrderByRemindAtAsc(applicationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FollowUpReminderResponse getReminder(Long applicationId, Long reminderId) {

        getOwnedApplication(applicationId);

        FollowUpReminder reminder = reminderRepository.findByIdAndJobApplication_Id(reminderId, applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reminder not found"
                        )
                );

        return mapToResponse(reminder);
    }

    public FollowUpReminderResponse updateReminder(Long applicationId, Long reminderId, FollowUpReminderRequest request) {

        getOwnedApplication(applicationId);

        FollowUpReminder reminder = reminderRepository.findByIdAndJobApplication_Id(reminderId, applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reminder not found"
                        )
                );

        reminder.setRemindAt(request.getRemindAt());
        reminder.setMessage(request.getMessage());
        reminder.setNotifiedAt(null);
        reminder.setStatus(ReminderStatus.PENDING);

        FollowUpReminder updatedReminder = reminderRepository.save(reminder);

        return mapToResponse(updatedReminder);
    }

    public FollowUpReminderResponse completeReminder(Long applicationId, Long reminderId) {

        getOwnedApplication(applicationId);

        FollowUpReminder reminder = reminderRepository.findByIdAndJobApplication_Id(reminderId, applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reminder not found"
                        )
                );

        reminder.setStatus(ReminderStatus.COMPLETED);

        FollowUpReminder updatedReminder = reminderRepository.save(reminder);

        return mapToResponse(updatedReminder);
    }

    public FollowUpReminderResponse cancelReminder(Long applicationId, Long reminderId) {

        getOwnedApplication(applicationId);

        FollowUpReminder reminder = reminderRepository.findByIdAndJobApplication_Id(reminderId, applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reminder not found"
                        )
                );

        reminder.setStatus(ReminderStatus.CANCELLED);

        FollowUpReminder updatedReminder = reminderRepository.save(reminder);

        return mapToResponse(updatedReminder);
    }

    public void deleteReminder(Long applicationId, Long reminderId) {

        getOwnedApplication(applicationId);

        FollowUpReminder reminder = reminderRepository.findByIdAndJobApplication_Id(reminderId, applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reminder not found"
                        )
                );

        reminderRepository.delete(reminder);
    }

    private FollowUpReminderResponse mapToResponse(FollowUpReminder reminder) {

        return new FollowUpReminderResponse(
                reminder.getId(),
                reminder.getRemindAt(),
                reminder.getMessage(),
                reminder.getStatus(),
                reminder.getNotifiedAt(),
                reminder.getCreatedAt(),
                reminder.getUpdatedAt()
        );
    }
}