package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.resume.CreateResumeDto;
import kg.attractor.job_search.dto.resume.EditResumeDto;
import kg.attractor.job_search.dto.resume.ResumeDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface ResumeService {
    List<ResumeDto> getResumes();

    List<ResumeDto> getActiveResumes();

    List<ResumeDto> getResumesByApplicantId(Long userId);

    List<ResumeDto> getResumesByApplicantName(String name);

    Long createResume(CreateResumeDto resumeDto);

    ResumeDto getResumeById(Long resumeId);

    Long updateResume(Long resumeId, EditResumeDto resumeDto);

    HttpStatus deleteResume(Long resumeId);

    List<ResumeDto> getResumesByCategoryId(Long categoryId);
}
