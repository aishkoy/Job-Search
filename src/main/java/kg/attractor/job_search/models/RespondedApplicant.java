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
}
