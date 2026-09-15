package com.abhishek.jobtracker.repository;

import com.abhishek.jobtracker.entity.FollowUpReminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowUpReminderRepository extends JpaRepository<FollowUpReminder, Long> {

    List<FollowUpReminder> findAllByJobApplication_IdOrderByRemindAtAsc(Long applicationId);

    Optional<FollowUpReminder> findByIdAndJobApplication_Id(Long id, Long applicationId);

    void deleteAllByJobApplication_Id(Long applicationId);
}