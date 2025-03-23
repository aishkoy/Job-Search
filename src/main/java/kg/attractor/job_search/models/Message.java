package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder

public class Message {
    private int id;
    private String content;
    private Timestamp timestamp;
    private int respondedApplicantId;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("id=").append(id);
        sb.append(", content='").append(content).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", respondedApplicantId=").append(respondedApplicantId);
        sb.append('}');
        return sb.toString();
    }
}
