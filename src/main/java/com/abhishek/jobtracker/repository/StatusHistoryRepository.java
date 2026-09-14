package com.abhishek.jobtracker.repository;

import com.abhishek.jobtracker.entity.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {

    List<StatusHistory>
    findAllByJobApplication_IdOrderByChangedAtAsc(
            Long applicationId
    );

    void deleteAllByJobApplication_Id(
            Long applicationId
    );
}