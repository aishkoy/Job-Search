package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ResumeDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface ResumeService {
    List<ResumeDto> getResumes();

    Long createResume(ResumeDto resumeDto);

    Long updateResume(Long resumeId, ResumeDto resumeDto);

    HttpStatus deleteResume(Long resumeId);

    List<ResumeDto> getResumesByCategoryId(Long categoryId);
}
