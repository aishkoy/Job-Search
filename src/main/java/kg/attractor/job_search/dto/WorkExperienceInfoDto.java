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
public class WorkExperienceInfoDto {
    private Long id;
    @Positive
    private Long resumeId;
    @NotNull @Positive
    private Integer years;
    @NotBlank
    private String companyName;
    @NotBlank
    private String position;
    @NotBlank
    private String responsibilities;
}
