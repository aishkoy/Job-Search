package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface WorkExperienceInfoService {
    Long createWorkExperience(WorkExperienceInfoDto createWorkExperienceInfoDto);

    List<WorkExperienceInfoDto> getWorkExperienceInfoByResumeId(Long resumeId);

    Long updateWorkExperienceInfo(Long id, WorkExperienceInfoDto createWorkExperienceInfoDto);

    WorkExperienceInfoDto getWorkExperienceById(Long id);

    HttpStatus deleteWorkExperienceInfo(Long id);
}
