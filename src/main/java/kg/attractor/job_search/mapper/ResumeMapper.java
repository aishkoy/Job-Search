package kg.attractor.job_search.mapper;
import kg.attractor.job_search.dto.resume.ResumeFormDto;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.model.Resume;
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
                .createdDate(resume.getCreatedDate())
                .updateTime(resume.getUpdateTime())
                .build();
    }

    public static Resume toResume(ResumeFormDto resumeDto) {
        return Resume.builder()
                .applicantId(resumeDto.getApplicantId())
                .name(resumeDto.getName())
                .categoryId(resumeDto.getCategoryId())
                .salary(resumeDto.getSalary())
                .build();
    }
}
