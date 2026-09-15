package com.abhishek.jobtracker.service;

import com.abhishek.jobtracker.entity.FollowUpReminder;
import com.abhishek.jobtracker.entity.ReminderStatus;
import com.abhishek.jobtracker.repository.FollowUpReminderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderSchedulerService {

    private final FollowUpReminderRepository reminderRepository;
    private final NotificationService notificationService;

    public ReminderSchedulerService(FollowUpReminderRepository reminderRepository, NotificationService notificationService) {
        this.reminderRepository = reminderRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelayString = "${reminder.scheduler.delay:60000}")
    @Transactional
    public void processDueReminders() {

        LocalDateTime now = LocalDateTime.now();

        List<FollowUpReminder> dueReminders =
                reminderRepository
                        .findAllByStatusAndRemindAtLessThanEqualAndNotifiedAtIsNullOrderByRemindAtAsc(
                                ReminderStatus.PENDING,
                                now
                        );

        for (FollowUpReminder reminder : dueReminders) {

            notificationService.sendReminderNotification(reminder);
            reminder.setNotifiedAt(now);
            reminderRepository.save(reminder);
        }
    }
}