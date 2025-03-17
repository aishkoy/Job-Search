package kg.attractor.job_search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResumeDto {
    private Long id;
    private Long applicantId;
    private String name;
    private Long categoryId;
    private Float salary;
    private Boolean isActive;
    private LocalDateTime createdData;
    private LocalDateTime updateTime;
}
