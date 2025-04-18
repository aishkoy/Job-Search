package kg.attractor.job_search.dto.resume;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import kg.attractor.job_search.dto.CategoryDto;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.dto.user.UserDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ResumeFormDto {
    @NotNull
    UserDto applicant;

    @NotBlank
    String name;

    @NotNull
    CategoryDto category;

    @NotNull
    @PositiveOrZero
    Float salary;

    Boolean isActive;

    private Boolean isActive;

    @Valid
    List<WorkExperienceInfoDto> workExperiences;

    @Valid
    List<EducationInfoDto> educations;

    @Valid
    List<ContactInfoDto> contacts;
}
