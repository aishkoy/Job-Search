package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.entity.ContactType;
import kg.attractor.job_search.validation.ValidContactValue;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

@ValidContactValue(typeIdField = "contactType.id", contactValueField = "contactValue")
public class ContactInfoDto {
    Long id;

    ResumeDto resume;

    @NotNull
    ContactType contactType;

    @NotBlank
    String contactValue;
}

