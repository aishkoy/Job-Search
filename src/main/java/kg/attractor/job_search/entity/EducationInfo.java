package kg.attractor.job_search.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "education_info")
public class EducationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id",
            nullable = false)
    Resume resume;

    @Column(name = "institution",
            nullable = false)
    String institution;

    @Column(name = "program",
            nullable = false)
    String program;

    @Column(name = "start_date",
            nullable = false)
    Date startDate;

    @Column(name = "end_date",
            nullable = false)
    Date endDate;

    @Column(name = "degree",
            nullable = false)
    String degree;
}
