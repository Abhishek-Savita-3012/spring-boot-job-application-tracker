package com.abhishek.jobtracker.repository;

import com.abhishek.jobtracker.entity.ApplicationStatus;
import com.abhishek.jobtracker.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.time.LocalDate;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>, JpaSpecificationExecutor<JobApplication> {

    Optional<JobApplication> findByIdAndUser_Id(
            Long id,
            Long userId
    );

    long countByUser_IdAndArchivedFalse(Long userId);

    long countByUser_IdAndArchivedTrue(Long userId);

    long countByUser_IdAndArchivedFalseAndStatus(Long userId, ApplicationStatus status);

    long countByUser_IdAndArchivedFalseAndAppliedDateGreaterThanEqualAndAppliedDateLessThan(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("""
       SELECT j.status, COUNT(j)
       FROM JobApplication j
       WHERE j.user.id = :userId
       AND j.archived = false
       GROUP BY j.status
       """)
    List<Object[]> countApplicationsByStatus(@Param("userId") Long userId);

    @Query("""
       SELECT j.workMode, COUNT(j)
       FROM JobApplication j
       WHERE j.user.id = :userId
       AND j.archived = false
       AND j.workMode IS NOT NULL
       GROUP BY j.workMode
       """)
    List<Object[]> countApplicationsByWorkMode(@Param("userId") Long userId);

    @Query("""
       SELECT j.employmentType, COUNT(j)
       FROM JobApplication j
       WHERE j.user.id = :userId
       AND j.archived = false
       AND j.employmentType IS NOT NULL
       GROUP BY j.employmentType
       """)
    List<Object[]> countApplicationsByEmploymentType(@Param("userId") Long userId);
}