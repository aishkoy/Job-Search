package kg.attractor.job_search.dto.resume;

import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ResumeDto {
    private Long id;
    private Long applicantId;
    private String name;
    private Long categoryId;
    private Float salary;
    private Boolean isActive;
    private Timestamp createdDate;
    private Timestamp updateTime;
    private List<WorkExperienceInfoDto> workExperiences;
    private List<EducationInfoDto> educations;
    private List<ContactInfoDto> contacts;
}
