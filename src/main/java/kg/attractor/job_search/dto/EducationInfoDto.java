package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.validation.ValidDateRange;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

@ValidDateRange(message = "Дата начала обучения должна быть раньше даты окончания")
public class EducationInfoDto {

    Long id;

    ResumeDto resume;

    @NotBlank
    String institution;

    @NotBlank
    String program;

    @NotNull
    @Past
    Date startDate;

    @NotNull
    Date endDate;

    @NotBlank
    String degree;
}

