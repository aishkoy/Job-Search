package kg.attractor.job_search.service.interfaces;

import kg.attractor.job_search.dto.EducationInfoDto;
import org.springframework.http.HttpStatus;

public interface EducationInfoService {
    Long createEducationInfo(EducationInfoDto educationInfoDto);

    Long updateEducationInfo(EducationInfoDto educationInfoDto);

    HttpStatus deleteEducationInfo(Long id);

    EducationInfoDto getEducationInfoById(Long id);
}
