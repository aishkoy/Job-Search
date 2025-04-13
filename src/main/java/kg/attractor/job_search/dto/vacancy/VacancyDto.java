package kg.attractor.job_search.dto.vacancy;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VacancyDto {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private Float salary;
    private Integer expFrom;
    private Integer expTo;
    private Boolean isActive;
    private Long authorId;
    private String authorName;
    private Timestamp createdDate;
    private Timestamp updateTime;
}
