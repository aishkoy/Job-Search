package kg.attractor.job_search.models;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Message {
    private Long id;
    private String content;
    private Timestamp timestamp;
    private Long respondedApplicantId;
}
