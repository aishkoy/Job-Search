package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.dto.resume.ResumeFormDto;
import kg.attractor.job_search.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface ResumeService {
    List<ResumeDto> getResumes();

    List<ResumeDto> getActiveResumes();

    List<ResumeDto> getResumesByApplicantId(Long userId);

    List<ResumeDto> getResumesByApplicantName(String name);

    Long createResume(ResumeFormDto resumeDto);

    Resume getResumeById(Long resumeId, Long userId);

    Resume getResumeById(Long resumeId);

    ResumeDto getResumeDtoById(Long resumeId, Long userId);

    ResumeDto getResumeDtoById(Long resumeId);

    Long updateResume(Long resumeId, ResumeFormDto resumeDto);

    void addExperience(ResumeFormDto resumeForm);

    void addEducation(ResumeFormDto resumeForm);

    void addContact(ResumeFormDto resumeForm);

    HttpStatus deleteResume(Long resumeId, Long userId);

    List<ResumeDto> getResumesByCategoryId(Long categoryId);

    Page<ResumeDto> getResumesPage(int page, int size);

    Page<ResumeDto> getActiveResumesPage(int page, int size);

    Page<ResumeDto> getResumesPageByApplicantId(int page, int size, Long applicantId);

    Page<ResumeDto> getResumesPageByApplicantName(int page, int size, String applicantName);

    Page<ResumeDto> getResumesPageByCategoryId(int page, int size, Long categoryId);

    ResumeFormDto convertToFormDto(ResumeDto dto);
}
