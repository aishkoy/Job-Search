package kg.attractor.job_search.dto.vacancy;

import kg.attractor.job_search.dto.CategoryDto;
import kg.attractor.job_search.dto.user.UserDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VacancyDto {
    Long id;
    String name;
    String description;
    CategoryDto category;
    Float salary;
    Integer expFrom;
    Integer expTo;
    Boolean isActive;
    UserDto employer;
    Timestamp createdAt;
    Timestamp updatedAt;
}
