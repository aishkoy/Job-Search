package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.RespondedApplicantDto;
import kg.attractor.job_search.entity.RespondedApplicant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RespondedApplicantMapper {
    @Mapping(target = "vacancy.responses", ignore = true)
    RespondedApplicantDto toDto(RespondedApplicant applicant);
    RespondedApplicant toEntity(RespondedApplicantDto dto);
}
