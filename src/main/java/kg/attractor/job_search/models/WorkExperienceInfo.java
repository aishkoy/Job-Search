package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkExperienceInfo {
    private int id;
    private int resumeId;
    private int years;
    private String companyName;
    private String position;
    private String responsibilities;
}
