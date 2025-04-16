package kg.attractor.job_search.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "work_experience_info")
public class WorkExperienceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id",
            nullable = false)
    Resume resume;

    @Column(name = "years",
            nullable = false)
    Integer years;

    @Column(name = "company_name",
            nullable = false)
    String companyName;

    @Column(name = "position",
            nullable = false)
    String position;

    @Column(name = "responsibilities")
    String responsibilities;
}
