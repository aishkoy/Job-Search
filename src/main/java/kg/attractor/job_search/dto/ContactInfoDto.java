package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kg.attractor.job_search.validation.ValidContactValue;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@ValidContactValue
public class ContactInfoDto {
    private Long id;
    private Long resumeId;
    @NotNull @Positive
    private Long typeId;
    @NotBlank
    private String contactValue;
}

