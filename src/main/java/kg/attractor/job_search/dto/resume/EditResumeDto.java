package kg.attractor.job_search.dto.resume;

import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EditResumeDto {
    private String name;
    private Long categoryId;
    private Float salary;
    private Boolean isActive;
    private List<EducationInfoDto> educations;
    private List<WorkExperienceInfoDto> workExperiences;
}
