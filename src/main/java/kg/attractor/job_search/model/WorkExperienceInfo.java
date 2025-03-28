package kg.attractor.job_search.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class WorkExperienceInfo {
    private Long id;
    private Long resumeId;
    private Integer years;
    private String companyName;
    private String position;
    private String responsibilities;
}
