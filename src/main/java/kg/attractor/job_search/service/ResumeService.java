package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ResumeDto;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public interface ResumeService {
    List<ResumeDto> getResumes();

    List<ResumeDto> getActiveResumes();

    List<ResumeDto> getResumesByApplicantId(Long userId);

    List<ResumeDto> getResumesByApplicantName(String name);

    Long createResume(ResumeDto resumeDto);

    Optional<ResumeDto> getResumeById(Long resumeId);

    Long updateResume(Long resumeId, ResumeDto resumeDto);

    HttpStatus deleteResume(Long resumeId);

    List<ResumeDto> getResumesByCategoryId(Long categoryId);
}
