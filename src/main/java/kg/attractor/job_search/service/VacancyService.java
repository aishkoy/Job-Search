package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.vacancy.CreateVacancyDto;
import kg.attractor.job_search.dto.vacancy.EditVacancyDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface VacancyService {
    Long createVacancy(CreateVacancyDto vacancyDto);

    List<VacancyDto> getVacancies();

    List<VacancyDto> getActiveVacancies();
    Long updateVacancy(Long vacancyId, EditVacancyDto vacancyDto);

    HttpStatus deleteVacancy(Long vacancyId);

    List<VacancyDto> getVacanciesByCategoryId(Long categoryId);

    VacancyDto getVacancyById(Long vacancyId);

    List<VacancyDto> getVacanciesAppliedByUserId(Long applicantId);

    List<VacancyDto> getVacanciesByEmployerId(Long employerId);

    List<VacancyDto> getVacanciesByCategoryName(String categoryName);
}
