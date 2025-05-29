package kg.attractor.job_search.service.interfaces;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ResumeService {
    Long createResume(ResumeDto resumeDto);

    Resume getResumeById(Long resumeId, Long userId);

    Resume getResumeById(Long resumeId);

    ResumeDto getResumeDtoById(Long resumeId, Long userId);

    ResumeDto getResumeDtoById(Long resumeId);

    @Transactional
    void updateResume(Long resumeId, Long userId);

    Long updateResume(Long resumeId, ResumeDto resumeDto);

    HttpStatus deleteResume(Long resumeId, Long userId);

    List<ResumeDto> getLastResumes(Integer limit);

    Page<ResumeDto> getActiveResumesPage(String query, int page, int size, Long categoryId, String sortBy, String sortDirection);

    Page<ResumeDto> getResumesByApplicantId(Long applicantId, int page, int size);
}
