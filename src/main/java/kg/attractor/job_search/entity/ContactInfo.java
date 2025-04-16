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
@Table(name = "contact_info")
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id",
            nullable = false)
    ContactType contactType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id",
            nullable = false)
    Resume resume;

    @Column(name = "contact_value",
            nullable = false)
    String contactValue;
}
