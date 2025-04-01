package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.vacancy.VacancyFormDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VacancyService {
    Long createVacancy(VacancyFormDto vacancyDto);

    List<VacancyDto> getVacancies();

    List<VacancyDto> getActiveVacancies();
    Long updateVacancy(Long vacancyId, VacancyFormDto vacancyDto);

    HttpStatus deleteVacancy(Long vacancyId, Long authorId);

    List<VacancyDto> getVacanciesByCategoryId(Long categoryId);

    VacancyDto getVacancyById(Long vacancyId);

    List<VacancyDto> getVacanciesAppliedByUserId(Long applicantId);

    List<VacancyDto> getVacanciesByEmployerId(Long employerId);

    List<VacancyDto> getVacanciesByCategoryName(String categoryName);

    Long changeActiveStatus(Long vacancyId, Long authorId);
}
