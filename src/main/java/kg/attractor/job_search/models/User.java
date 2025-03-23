package kg.attractor.job_search.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private String email;
    private String password;
    private String phoneNumber;
    private String avatar;
    private String accountType;
}
