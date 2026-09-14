package com.abhishek.jobtracker.repository;

import com.abhishek.jobtracker.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

    Optional<Resume> findByIdAndUser_Id(Long id, Long userId);
}