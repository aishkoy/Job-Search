package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationInfoDto {
    private Long id;
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

