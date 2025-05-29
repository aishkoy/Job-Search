package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.entity.WorkExperienceInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkExperienceMapper {
    @Mapping(target = "resume", ignore = true)
    WorkExperienceInfo toEntity(WorkExperienceInfoDto workExperienceInfoDto);

    @Mapping(target = "resumeId", source = "resume.id")
    WorkExperienceInfoDto toDto(WorkExperienceInfo workExperienceInfo);
}