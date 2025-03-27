package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.model.EducationInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EducationInfoMapper {
    public EducationInfo toEducationInfo(EducationInfoDto educationInfoDto){
        return EducationInfo.builder()
                .id(educationInfoDto.getId())
                .resumeId(educationInfoDto.getResumeId())
                .institution(educationInfoDto.getInstitution())
                .program(educationInfoDto.getProgram())
                .startDate(educationInfoDto.getStartDate())
                .endDate(educationInfoDto.getEndDate())
                .degree(educationInfoDto.getDegree())
                .build();
    }

    public EducationInfoDto toEducationInfoDto(EducationInfo educationInfo){
        return EducationInfoDto.builder()
                .id(educationInfo.getId())
                .resumeId(educationInfo.getResumeId())
                .institution(educationInfo.getInstitution())
                .program(educationInfo.getProgram())
                .startDate(educationInfo.getStartDate())
                .endDate(educationInfo.getEndDate())
                .degree(educationInfo.getDegree())
                .build();
    }

}
