package kg.attractor.job_search.models;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Vacancy {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private Float salary;
    private Integer expFrom;
    private Integer expTo;
    private Boolean isActive;
    private Long authorId;
    private Timestamp createdDate;
    private Timestamp updateTime;
}
