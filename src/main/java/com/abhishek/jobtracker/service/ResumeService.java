package com.abhishek.jobtracker.service;

import com.abhishek.jobtracker.dto.ResumeRequest;
import com.abhishek.jobtracker.dto.ResumeResponse;
import com.abhishek.jobtracker.entity.Resume;
import com.abhishek.jobtracker.entity.User;
import com.abhishek.jobtracker.exception.ResourceNotFoundException;
import com.abhishek.jobtracker.repository.ResumeRepository;
import com.abhishek.jobtracker.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final CurrentUserService currentUserService;

    public ResumeService(ResumeRepository resumeRepository, CurrentUserService currentUserService) {
        this.resumeRepository = resumeRepository;
        this.currentUserService = currentUserService;
    }

    public ResumeResponse createResume(ResumeRequest request) {

        User user = currentUserService.getCurrentUser();

        Resume resume = Resume.builder()
                .label(request.getLabel())
                .originalFileName(request.getOriginalFileName())
                .externalUrl(request.getExternalUrl())
                .user(user)
                .build();

        Resume savedResume = resumeRepository.save(resume);

        return mapToResponse(savedResume);
    }

    public List<ResumeResponse> getAllResumes() {

        User user = currentUserService.getCurrentUser();

        return resumeRepository.findAllByUser_IdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ResumeResponse getResume(Long resumeId) {

        User user = currentUserService.getCurrentUser();

        Resume resume = resumeRepository.findByIdAndUser_Id(resumeId, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resume not found"
                        )
                );

        return mapToResponse(resume);
    }

    private ResumeResponse mapToResponse(Resume resume) {

        return new ResumeResponse(
                resume.getId(),
                resume.getLabel(),
                resume.getOriginalFileName(),
                resume.getExternalUrl(),
                resume.getCreatedAt(),
                resume.getUpdatedAt()
        );
    }
}