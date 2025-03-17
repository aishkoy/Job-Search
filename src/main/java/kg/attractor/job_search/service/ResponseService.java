package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.UserDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface ResponseService {
    List<UserDto> getApplicationsByVacancyId(Long vacancyId, Long employerId);

    HttpStatus applyVacancy(Long vacancyId, Long applicantId);
}
