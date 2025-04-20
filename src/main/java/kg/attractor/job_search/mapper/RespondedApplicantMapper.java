package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.RespondedApplicantDto;
import kg.attractor.job_search.entity.RespondedApplicant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RespondedApplicantMapper {
    RespondedApplicantDto toDto(RespondedApplicant applicant);
    RespondedApplicant toEntity(RespondedApplicantDto dto);
}
