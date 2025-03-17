package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.VacancyDto;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface VacancyService {
    Long createVacancy(VacancyDto vacancyDto, Long employerId);

    List<VacancyDto> getActiveVacancies(Long applicantId);
    Long updateVacancy(Long vacancyId, VacancyDto vacancyDto, Long employerId);

    HttpStatus deleteVacancy(Long vacancyId, Long employerId);

    @SneakyThrows
    List<VacancyDto> getVacanciesByCategoryId(Long categoryId, Long applicantId);
}
