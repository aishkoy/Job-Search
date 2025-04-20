package kg.attractor.job_search.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "resumes")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            nullable = false)
    User applicant;

    @Column(nullable = false,
            name = "name")
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",
            nullable = false)
    Category category;

    @Column(nullable = false,
            name = "salary")
    Float salary;

    @Column(nullable = false,
            name = "is_active")
    @Builder.Default
    Boolean isActive = true;

    @Column(nullable = false,
            name = "created_at")
    @Builder.Default
    Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at")
    @Builder.Default
    Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EducationInfo> educations;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    List<WorkExperienceInfo> workExperiences;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ContactInfo> contacts;
}
