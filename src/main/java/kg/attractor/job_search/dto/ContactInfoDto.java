package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kg.attractor.job_search.validation.annotation.ValidContactValue;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

@ValidContactValue(typeIdField = "contactType.id")
public class ContactInfoDto {
    Long id;

    Long resumeId;

    @NotNull
    ContactTypeDto contactType;

    @NotBlank
    String contactValue;
}

