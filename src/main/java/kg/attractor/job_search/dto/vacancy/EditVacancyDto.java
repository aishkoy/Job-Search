package kg.attractor.job_search.dto.vacancy;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EditVacancyDto {
    private String name;
    private String description;
    private Long categoryId;
    private Float salary;
    private Integer expFrom;
    private Integer expTo;
    private Boolean isActive;
}