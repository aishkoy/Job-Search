package kg.attractor.job_search.dto.resume;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ResumeFormDto {
    @NotNull
    private Long applicantId;
    @NotBlank
    private String name;
    @NotNull
    private Long categoryId;
    @NotNull @PositiveOrZero
    private Float salary;

    @Valid
    private List<WorkExperienceInfoDto> workExperiences;

    @Valid
    private List<EducationInfoDto> educations;

    @Valid
    private List<ContactInfoDto> contacts;
}
