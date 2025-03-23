package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.VacancyDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface VacancyService {
    Long createVacancy(VacancyDto vacancyDto);

    List<VacancyDto> getVacancies();

    List<VacancyDto> getActiveVacancies();
    Long updateVacancy(Long vacancyId, VacancyDto vacancyDto);

    HttpStatus deleteVacancy(Long vacancyId);

    List<VacancyDto> getVacanciesByCategoryId(Long categoryId);

    VacancyDto getVacancyById(Long vacancyId);

    List<VacancyDto> getVacanciesAppliedByUserId(Long applicantId);

    List<VacancyDto> getVacanciesByEmployerId(Long employerId);

    List<VacancyDto> getVacanciesByCategoryName(String categoryName);
}
