package kg.attractor.job_search.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ResumeDto {
    private Long id;
    private Long applicantId;
    private String name;
    private Long categoryId;
    private Float salary;
    private Boolean isActive;
    private Timestamp createdDate;
    private Timestamp updateTime;
}
