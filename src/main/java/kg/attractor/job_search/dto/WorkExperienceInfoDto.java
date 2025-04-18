package kg.attractor.job_search.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kg.attractor.job_search.dto.resume.ResumeDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkExperienceInfoDto {
    Long id;

    ResumeDto resume;

    @NotNull
    @Positive
    @Max(80)
    Integer years;

    @NotBlank
    String companyName;

    @NotBlank
    String position;

    @NotBlank
    String responsibilities;
}
