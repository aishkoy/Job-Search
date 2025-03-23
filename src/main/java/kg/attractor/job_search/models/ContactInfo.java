package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactInfo {
    private int id;
    private int typeId;
    private int resumeId;
    private String value;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContactInfo{");
        sb.append("id=").append(id);
        sb.append(", typeId=").append(typeId);
        sb.append(", resumeId=").append(resumeId);
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
