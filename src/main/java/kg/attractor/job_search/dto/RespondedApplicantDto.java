package kg.attractor.job_search.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RespondedApplicantDto {
    private Long id;
    private Long resumeId;
    private Long vacancyId;
    private Boolean confirmation;
}
