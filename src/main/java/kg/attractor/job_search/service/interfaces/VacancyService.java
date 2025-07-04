package kg.attractor.job_search.service.interfaces;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.entity.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface VacancyService {
    Long createVacancy(VacancyDto vacancyDto);

    void updateVacancy(Long vacancyId, Long userId);

    Long updateVacancy(Long vacancyId, VacancyDto vacancyDto);

    HttpStatus deleteVacancy(Long vacancyId, Long authorId);

    List<VacancyDto> getLastVacancies(Integer limit);

    VacancyDto getVacancyDtoById(Long vacancyId);

    Vacancy getVacancyById(Long vacancyId);

    VacancyDto getVacancyDtoByIdAndAuthor(Long vacancyId, Long authorId);

    Page<VacancyDto> getVacanciesByEmployerId(Long employer, int page, int size);

    Page<VacancyDto> getActiveVacanciesPage(String query, int page, int size, Long categoryId, String sortBy, String sortDirection);
}
