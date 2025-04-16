package kg.attractor.job_search.dto.user;

import kg.attractor.job_search.dto.RoleDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserDto {
    Long id;
    String name;
    String surname;
    Integer age;
    String email;
    String password;
    String phoneNumber;
    String avatar;
    Boolean enabled;
    RoleDto role;
}
