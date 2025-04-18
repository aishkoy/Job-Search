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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false,
            name = "name")
    String name;

    @Column(nullable = false,
            name = "surname")
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
    @Builder.Default
    String avatar = null;

    @Column(nullable = false,
            name = "enabled")
    @Builder.Default
    Boolean enabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",
            nullable = false)
    Role role;
}
