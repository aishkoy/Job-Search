package kg.attractor.job_search.dto.resume;

import kg.attractor.job_search.dto.CategoryDto;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.dto.user.UserDto;
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

public class ResumeDto {
     Long id;
     UserDto applicant;
     String name;
     CategoryDto category;
     Float salary;
     Boolean isActive;
     Timestamp createdAt;
     Timestamp updatedAt;
     List<WorkExperienceInfoDto> workExperiences;
     List<EducationInfoDto> educations;
     List<ContactInfoDto> contacts;
}
