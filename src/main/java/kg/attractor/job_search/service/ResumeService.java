package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.dto.resume.ResumeFormDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface ResumeService {
    List<ResumeDto> getResumes();

    List<ResumeDto> getActiveResumes();

    List<ResumeDto> getResumesByApplicantId(Long userId);

    List<ResumeDto> getResumesByApplicantName(String name);

    Long createResume(ResumeFormDto resumeDto);

    ResumeDto getResumeById(Long resumeId);

    Long updateResume(Long resumeId, ResumeFormDto resumeDto);

    HttpStatus deleteResume(Long resumeId, Long userId);

    List<ResumeDto> getResumesByCategoryId(Long categoryId);

    Long changeActiveStatus(Long resumeId, Long authId);
}
