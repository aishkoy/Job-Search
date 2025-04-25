package kg.attractor.job_search.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import kg.attractor.job_search.dto.user.SimpleUserDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ResumeDto {
    Long id;

    @NotNull
    SimpleUserDto applicant;

    @NotBlank
    String name;

    @NotNull
    CategoryDto category;

    @NotNull
    @PositiveOrZero
    Float salary;

    @Builder.Default
    Boolean isActive = true;

    @Builder.Default
    Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Builder.Default
    Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

    @Valid
    @Builder.Default
    List<WorkExperienceInfoDto> workExperiences = new ArrayList<>();

    @Valid
    @Builder.Default
    List<EducationInfoDto> educations = new ArrayList<>();

    @Valid
    @Builder.Default
    List<ContactInfoDto> contacts = new ArrayList<>();
}
