package com.abhishek.jobtracker.repository;

import com.abhishek.jobtracker.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>, JpaSpecificationExecutor<JobApplication> {
    List<JobApplication> findAllByUser_Id(Long userId);

    Optional<JobApplication> findByIdAndUser_Id(
            Long id,
            Long userId
    );
}