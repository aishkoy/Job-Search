package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class MessageDto {
    Long id;

    @NotBlank
    String content;

    @NotNull @NotBlank
    Timestamp timestamp;

    @NotNull
    RespondedApplicantDto respondedApplicant;
}
