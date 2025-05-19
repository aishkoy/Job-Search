package kg.attractor.job_search.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Lob
    @Column(name = "content",
            nullable = false)
    String content;

    @Column(name = "timestamp",
            nullable = false)
    Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "response_id",
            nullable = false)
    Response response;

    @Column(name = "is_read",
            nullable = false)
    Boolean isRead;

    @Column(name = "is_applicant",
            nullable = false)
    Boolean isApplicant;
}
