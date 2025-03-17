package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.models.Resume;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResumeMapper {
    public ResumeDto toResumeDto(Resume resume) {
        return ResumeDto.builder()
                .id(resume.getId())
                .applicantId(resume.getApplicantId())
                .name(resume.getName())
                .categoryId(resume.getCategoryId())
                .salary(resume.getSalary())
                .isActive(resume.getIsActive())
                .createdData(resume.getCreatedData())
                .updateTime(resume.getUpdateTime())
                .build();
    }

    public Resume toResume(ResumeDto resumeDto) {
        return Resume.builder()
                .id(resumeDto.getId())
                .applicantId(resumeDto.getApplicantId())
                .name(resumeDto.getName())
                .categoryId(resumeDto.getCategoryId())
                .salary(resumeDto.getSalary())
                .isActive(resumeDto.getIsActive())
                .createdData(resumeDto.getCreatedData())
                .updateTime(resumeDto.getUpdateTime())
                .build();
    }
}
