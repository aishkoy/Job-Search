package kg.attractor.job_search.models;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Resume {
    private Long id;
    private Long applicantId;
    private String name;
    private Long categoryId;
    private Float salary;
    private Boolean isActive;
    private Timestamp createdDate;
    private Timestamp updateTime;
}
