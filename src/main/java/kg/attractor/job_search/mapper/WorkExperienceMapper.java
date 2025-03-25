package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.models.WorkExperienceInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WorkExperienceMapper {
    public WorkExperienceInfo toWorkExperienceInfo(WorkExperienceInfoDto workExperienceInfoDto) {
        return WorkExperienceInfo.builder()
                .id(workExperienceInfoDto.getId())
                .resumeId(workExperienceInfoDto.getResumeId())
                .years(workExperienceInfoDto.getYears())
                .companyName(workExperienceInfoDto.getCompanyName())
                .position(workExperienceInfoDto.getPosition())
                .responsibilities(workExperienceInfoDto.getResponsibilities())
                .build();
    }


    public WorkExperienceInfoDto toWorkExperienceInfoDto(WorkExperienceInfo workExperienceInfo) {
        return WorkExperienceInfoDto.builder()
                .id(workExperienceInfo.getId())
                .resumeId(workExperienceInfo.getResumeId())
                .years(workExperienceInfo.getYears())
                .companyName(workExperienceInfo.getCompanyName())
                .position(workExperienceInfo.getPosition())
                .responsibilities(workExperienceInfo.getResponsibilities())
                .build();
    }
}
