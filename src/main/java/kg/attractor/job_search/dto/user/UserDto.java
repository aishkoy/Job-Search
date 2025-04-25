package kg.attractor.job_search.dto.user;

import kg.attractor.job_search.dto.RoleDto;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.VacancyDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserDto extends SimpleUserDto {
    String email;
    String password;
    String avatar;
    Boolean enabled;
    RoleDto role;

    @Builder.Default
    List<VacancyDto> vacancies = new ArrayList<>();

    @Builder.Default
    List<ResumeDto> resumes = new ArrayList<>();
}