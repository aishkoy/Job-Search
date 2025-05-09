package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import kg.attractor.job_search.dto.user.SimpleUserDto;
import kg.attractor.job_search.validation.ValidExperienceRange;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

@ValidExperienceRange
public class VacancyDto {
    Long id;

    @NotBlank
    String name;

    String description;

    @NotNull
    CategoryDto category;

    @NotNull
    @PositiveOrZero
    Float salary;

    @NotNull
    @Positive
    Integer expFrom;

    @NotNull
    @Positive
    Integer expTo;

    @Builder.Default
    Boolean isActive = true;

    @NotNull
    SimpleUserDto employer;

    @Builder.Default
    Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Builder.Default
    Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

    List<RespondedApplicantDto> responses;
}
