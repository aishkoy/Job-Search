package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.VacancyDto;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface VacancyService {
    Long createVacancy(VacancyDto vacancyDto);

    List<VacancyDto> getActiveVacancies();
    Long updateVacancy(Long vacancyId, VacancyDto vacancyDto);

    HttpStatus deleteVacancy(Long vacancyId);

    @SneakyThrows
    List<VacancyDto> getVacanciesByCategoryId(Long categoryId);
}
