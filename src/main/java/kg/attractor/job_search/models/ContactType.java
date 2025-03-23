package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactType {
    private int id;
    private String type;
}
