package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.entity.EducationInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EducationInfoMapper {
    @Mapping(target = "resume", ignore = true)
    EducationInfo toEntity(EducationInfoDto educationInfoDto);

    @Mapping(target = "resumeId", source = "resume.id")
    EducationInfoDto toDto(EducationInfo educationInfo);
}