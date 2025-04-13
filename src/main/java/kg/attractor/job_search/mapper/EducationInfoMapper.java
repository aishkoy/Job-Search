package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.model.EducationInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EducationInfoMapper {
    EducationInfo toEntity(EducationInfoDto educationInfoDto);
    EducationInfoDto toDto(EducationInfo educationInfo);
}
