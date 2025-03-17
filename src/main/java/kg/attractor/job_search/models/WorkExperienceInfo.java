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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WorkExperienceInfo{");
        sb.append("id=").append(id);
        sb.append(", resumeId=").append(resumeId);
        sb.append(", years=").append(years);
        sb.append(", companyName='").append(companyName).append('\'');
        sb.append(", position='").append(position).append('\'');
        sb.append(", responsibilities='").append(responsibilities).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
