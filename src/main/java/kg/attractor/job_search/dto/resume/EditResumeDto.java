package kg.attractor.job_search.dto.resume;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EditResumeDto {
    private String name;
    private Long categoryId;
    private Float salary;
    private Boolean isActive;
}

