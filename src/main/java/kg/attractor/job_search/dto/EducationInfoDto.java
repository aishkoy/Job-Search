package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import kg.attractor.job_search.validation.ValidDateRange;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

@ValidDateRange
public class EducationInfoDto {

    Long id;

    Long resumeId;

    @NotBlank
    String institution;

    @NotBlank
    String program;

    @NotNull
    @Past
    LocalDate startDate;

    @NotNull
    LocalDate endDate;

    @NotBlank
    String degree;
}

