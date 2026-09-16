package com.abhishek.jobtracker.repository;

import com.abhishek.jobtracker.entity.FollowUpReminder;
import com.abhishek.jobtracker.entity.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FollowUpReminderRepository extends JpaRepository<FollowUpReminder, Long> {

    List<FollowUpReminder> findAllByJobApplication_IdOrderByRemindAtAsc(Long applicationId);

    Optional<FollowUpReminder> findByIdAndJobApplication_Id(Long id, Long applicationId);

    List<FollowUpReminder> findAllByStatusAndRemindAtLessThanEqualAndNotifiedAtIsNullOrderByRemindAtAsc(ReminderStatus status, LocalDateTime currentTime);

    void deleteAllByJobApplication_Id(Long applicationId);

    long countByJobApplication_User_IdAndJobApplication_ArchivedFalseAndStatus(
            Long userId,
            ReminderStatus status
    );
}