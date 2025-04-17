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
@Table(name = "vacancies")
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name",
            nullable = false)
    String name;

    @Lob
    @Column(name = "description")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",
            nullable = false)
    Category category;

    @Column(name = "salary",
            nullable = false)
    Float salary;

    @Column(name = "exp_from",
            nullable = false)
    Integer expFrom;

    @Column(name = "exp_to")
    Integer expTo;

    @Column(name = "is_active",
            nullable = false)
    Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            nullable = false)
    User employer;

    @Column(name = "created_at",
            nullable = false)
    Timestamp createdAt;

    @Column(name = "updated_at")
    Timestamp updatedAt;
}
