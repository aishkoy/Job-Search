package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.model.WorkExperienceInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkExperienceMapper {
    WorkExperienceInfo toEntity(WorkExperienceInfoDto workExperienceInfoDto);
    WorkExperienceInfoDto toDto(WorkExperienceInfo workExperienceInfo);
}
