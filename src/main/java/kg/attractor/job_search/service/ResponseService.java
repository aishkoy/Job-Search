package kg.attractor.job_search.service;

import org.springframework.http.HttpStatus;

public interface ResponseService {
    HttpStatus applyVacancy(Long vacancyId);
}
