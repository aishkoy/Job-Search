package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.EducationInfoDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface EducationInfoService {
    Long createEducationInfo(EducationInfoDto educationInfoDto);

    List<EducationInfoDto> getEducationInfoByResumeId(Long resumeId);

    Long updateEducationInfo(Long id, EducationInfoDto educationInfoDto);

    HttpStatus deleteEducationInfo(Long id);

    EducationInfoDto getEducationInfoById(Long id);
}
