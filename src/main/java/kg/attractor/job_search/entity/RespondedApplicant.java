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
@Table(name = "responded_applicants")
public class RespondedApplicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id",
            nullable = false)
    Resume resume;

    @ManyToOne
    @JoinColumn(name = "vacancy_id",
            nullable = false)
    Vacancy vacancy;

    @Column(name = "is_confirmed",
            nullable = false)
    Boolean isConfirmed;
}
