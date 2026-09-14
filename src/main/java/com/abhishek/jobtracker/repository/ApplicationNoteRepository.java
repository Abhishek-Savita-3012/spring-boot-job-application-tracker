package com.abhishek.jobtracker.repository;

import com.abhishek.jobtracker.entity.ApplicationNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationNoteRepository extends JpaRepository<ApplicationNote, Long> {

    List<ApplicationNote> findAllByJobApplication_IdOrderByCreatedAtDesc(Long applicationId);

    Optional<ApplicationNote> findByIdAndJobApplication_Id(Long id, Long applicationId);

    void deleteAllByJobApplication_Id(Long applicationId);
}