package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotNull;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class RespondedApplicantDto {
    Long id;

    @NotNull
    ResumeDto resume;

    @NotNull
    VacancyDto vacancy;

    @NotNull
    Boolean confirmation;
}
