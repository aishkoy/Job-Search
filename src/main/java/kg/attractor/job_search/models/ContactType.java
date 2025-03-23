package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactType {
    private int id;
    private String type;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContactType{");
        sb.append("id=").append(id);
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
