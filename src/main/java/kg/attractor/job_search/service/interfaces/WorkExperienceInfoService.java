package kg.attractor.job_search.service.interfaces;

import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import org.springframework.http.HttpStatus;

public interface WorkExperienceInfoService {
    Long createWorkExperience(WorkExperienceInfoDto createWorkExperienceInfoDto);
    Long updateWorkExperienceInfo(WorkExperienceInfoDto createWorkExperienceInfoDto);

    WorkExperienceInfoDto getWorkExperienceById(Long id);

    HttpStatus deleteWorkExperienceInfo(Long id);
}
