package kg.attractor.job_search.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EditUserDto {
    private String name;
    private String surname;
    private Integer age;
    private String phoneNumber;
    private String avatar;
}