package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;


@Data
@Builder
public class EducationInfo {
    private int id;
    private int resumeId;
    private String institution;
    private String program;
    private Date startDate;
    private Date endDate;
    private String degree;
}
