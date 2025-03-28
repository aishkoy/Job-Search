package kg.attractor.job_search.model;

import lombok.*;

import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EducationInfo {
    private Long id;
    private Long resumeId;
    private String institution;
    private String program;
    private Date startDate;
    private Date endDate;
    private String degree;
}
