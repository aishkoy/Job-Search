package kg.attractor.job_search.dto.vacancy;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreateVacancyDto {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Long categoryId;
    @NotNull @PositiveOrZero
    private Float salary;
    @NotNull @Positive
    private Integer expFrom;
    @NotNull @Positive
    private Integer expTo;
    @NotNull
    private Long authorId;
}
