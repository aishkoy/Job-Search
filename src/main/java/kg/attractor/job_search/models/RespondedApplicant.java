package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RespondedApplicant {
    private int id;
    private int resumeId;
    private int vacancyId;
    private boolean confirmation;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RespondedApplicant{");
        sb.append("id=").append(id);
        sb.append(", resumeId=").append(resumeId);
        sb.append(", vacancyId=").append(vacancyId);
        sb.append(", confirmation=").append(confirmation);
        sb.append('}');
        return sb.toString();
    }
}
