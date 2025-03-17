package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class EducationInfo {
    private int id;
    private int resumeId;
    private String institution;
    private String program;
    private LocalDate startDate;
    private LocalDate endDate;
    private String degree;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EducationInfo{");
        sb.append("id=").append(id);
        sb.append(", resumeId=").append(resumeId);
        sb.append(", institution='").append(institution).append('\'');
        sb.append(", program='").append(program).append('\'');
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", degree='").append(degree).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
