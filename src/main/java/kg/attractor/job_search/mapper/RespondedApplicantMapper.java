package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.RespondedApplicantDto;
import kg.attractor.job_search.model.RespondedApplicant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RespondedApplicantMapper {
    RespondedApplicantDto toRespondedApplicantDto(RespondedApplicant applicant);
    RespondedApplicant toRespondedApplicant(RespondedApplicantDto dto);
}
