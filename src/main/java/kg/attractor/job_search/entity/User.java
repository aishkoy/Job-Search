package kg.attractor.job_search.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false,
            name = "name")
    String name;

    @Column(name = "surname")
    String surname;

    @Column(nullable = false,
            name = "age")
    Integer age;

    @Column(nullable = false,
            name = "email",
            unique = true)
    String email;

    @Column(nullable = false,
            name = "password")
    String password;

    @Column(nullable = false,
            name = "phone_number")
    String phoneNumber;

    @Lob
    @Column(name = "avatar")
    String avatar;

    @Column(nullable = false,
            name = "enabled")
    Boolean enabled;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id",
            nullable = false)
    Role role;

    @Column(name = "reset_password_token")
    String resetPasswordToken;

    @Column(name = "prefer_lang",
            nullable = false)
    String preferredLanguage;

    @OneToMany(mappedBy = "employer")
    List<Vacancy> vacancies;

    @OneToMany(mappedBy = "applicant")
    List<Resume> resumes;
}
