package kg.attractor.job_search.service.interfaces;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.entity.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface VacancyService {
    Long createVacancy(VacancyDto vacancyDto);

    List<VacancyDto> getVacancies();

    Long updateVacancy(Long vacancyId, VacancyDto vacancyDto);

    HttpStatus deleteVacancy(Long vacancyId, Long authorId);

    List<VacancyDto> getActiveVacancies();

    List<VacancyDto> getVacanciesByCategoryId(Long categoryId);

    VacancyDto getVacancyDtoById(Long vacancyId);

    Vacancy getVacancyById(Long vacancyId);

    VacancyDto getVacancyDtoByIdAndAuthor(Long vacancyId, Long authorId);

    List<VacancyDto> getVacanciesAppliedByUserId(Long applicantId);

    List<VacancyDto> getVacanciesByEmployerId(Long employerId);

    List<VacancyDto> getVacanciesByCategoryName(String categoryName);

    List<VacancyDto> getLastVacancies();

    Page<VacancyDto> getVacanciesByEmployerId(Long employer, int page, int size);

    Page<VacancyDto> getActiveVacanciesPage(int page, int size, Long categoryId, String sortBy, String sortDirection);

    Page<VacancyDto> getVacanciesPageByCategoryId(int page, int size, Long categoryId, String sortBy, String sortDirection);
}
