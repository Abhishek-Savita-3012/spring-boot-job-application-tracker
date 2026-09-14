package com.abhishek.jobtracker.repository;

import com.abhishek.jobtracker.entity.InterviewRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewRoundRepository extends JpaRepository<InterviewRound, Long> {

    List<InterviewRound> findAllByJobApplication_IdOrderByRoundNumberAsc(Long applicationId);

    Optional<InterviewRound> findByIdAndJobApplication_Id(Long id, Long applicationId);

    void deleteAllByJobApplication_Id(
            Long applicationId
    );
}