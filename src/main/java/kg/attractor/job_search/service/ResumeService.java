package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface ResumeService {
    List<ResumeDto> getResumes();

    List<ResumeDto> getActiveResumes();

    List<ResumeDto> getResumesByApplicantId(Long userId);

    List<ResumeDto> getResumesByApplicantName(String name);

    Long createResume(ResumeDto resumeDto);

    Resume getResumeById(Long resumeId, Long userId);

    Resume getResumeById(Long resumeId);

    ResumeDto getResumeDtoById(Long resumeId, Long userId);

    ResumeDto getResumeDtoById(Long resumeId);

    Long updateResume(Long resumeId, ResumeDto resumeDto);

    void addExperience(ResumeDto resumeForm);

    void addEducation(ResumeDto resumeForm);

    void addContact(ResumeDto resumeForm);

    HttpStatus deleteResume(Long resumeId, Long userId);

    List<ResumeDto> getResumesByCategoryId(Long categoryId);

    List<ResumeDto> getLastResumes();

    Page<ResumeDto> getActiveResumesPage(int page, int size);

    Page<ResumeDto> getResumesPageByCategoryId(int page, int size, Long categoryId);
}
