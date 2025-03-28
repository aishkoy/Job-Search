package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ContactInfoDto {
    private Long id;
    @NotNull @Positive
    private Long typeId;
    @Positive
    private Long resumeId;
    @NotBlank
    private String contactValue;
}

