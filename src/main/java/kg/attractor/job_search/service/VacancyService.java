package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.vacancy.VacancyFormDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import kg.attractor.job_search.entity.Vacancy;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface VacancyService {
    Long createVacancy(VacancyFormDto vacancyDto);

    List<VacancyDto> getVacancies();

    List<VacancyDto> getActiveVacancies();

    Long updateVacancy(Long vacancyId, VacancyFormDto vacancyDto);

    HttpStatus deleteVacancy(Long vacancyId, Long authorId);

    List<VacancyDto> getVacanciesByCategoryId(Long categoryId);

    VacancyDto getVacancyDtoById(Long vacancyId);

    Vacancy getVacancyById(Long vacancyId);

    VacancyDto getVacancyDtoByIdAndAuthor(Long vacancyId, Long authorId);

    List<VacancyDto> getVacanciesAppliedByUserId(Long applicantId);

    List<VacancyDto> getVacanciesByEmployerId(Long employerId);

    List<VacancyDto> getVacanciesByCategoryName(String categoryName);

    List<VacancyDto> getLast3Vacancies();

    VacancyFormDto convertToFormDto(VacancyDto dto);
}
