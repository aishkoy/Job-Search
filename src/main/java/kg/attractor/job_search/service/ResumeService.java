package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ResumeDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface ResumeService {
    List<ResumeDto> getResumes(Long employerId);

    Long createResume(ResumeDto resumeDto, Long applicantId);

    Long updateResume(Long resumeId, ResumeDto resumeDto, Long applicantId);

    HttpStatus deleteResume(Long resumeId, Long applicantId);

    List<ResumeDto> getResumesByCategoryId(Long categoryId, Long employerId);
}
