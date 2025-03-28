package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.RespondedApplicantDto;
import kg.attractor.job_search.model.RespondedApplicant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RespondedApplicantMapper {
    public static RespondedApplicantDto toRespondedApplicantDto(RespondedApplicant applicant) {
        return RespondedApplicantDto.builder()
                .id(applicant.getId())
                .resumeId(applicant.getResumeId())
                .vacancyId(applicant.getVacancyId())
                .confirmation(applicant.getConfirmation())
                .build();
    }

    public static RespondedApplicant toRespondedApplicant(RespondedApplicantDto dto) {
        return RespondedApplicant.builder()
                .id(dto.getId())
                .resumeId(dto.getResumeId())
                .vacancyId(dto.getVacancyId())
                .confirmation(dto.getConfirmation())
                .build();
    }
}
