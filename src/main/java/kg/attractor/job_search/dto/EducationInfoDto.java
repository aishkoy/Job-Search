package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EducationInfoDto {
    private Long id;
    @Positive
    private Long resumeId;
    @NotBlank
    private String institution;
    @NotBlank
    private String program;
    @NotNull @Past
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotBlank
    private String degree;
}

