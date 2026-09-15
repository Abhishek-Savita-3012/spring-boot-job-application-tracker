package com.abhishek.jobtracker.service;

import com.abhishek.jobtracker.entity.FollowUpReminder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void sendReminderNotification(FollowUpReminder reminder) {

        logger.info(
                "REMINDER NOTIFICATION | User: {} | Company: {} | Role: {} | Message: {}",
                reminder.getJobApplication()
                        .getUser()
                        .getEmail(),
                reminder.getJobApplication()
                        .getCompany(),
                reminder.getJobApplication()
                        .getRole(),
                reminder.getMessage()
        );
    }
}