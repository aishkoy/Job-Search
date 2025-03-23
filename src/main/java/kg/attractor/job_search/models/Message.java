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
}
