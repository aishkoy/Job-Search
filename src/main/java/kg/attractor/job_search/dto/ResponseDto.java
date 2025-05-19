package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ResponseDto {
    Long id;

    @NotNull
    ResumeDto resume;

    @NotNull
    VacancyDto vacancy;

    @NotNull
    @Builder.Default
    Boolean isConfirmed = false;
}
