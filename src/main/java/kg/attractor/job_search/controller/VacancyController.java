package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    @GetMapping
    public ResponseEntity<List<VacancyDto>> getVacancies(@RequestParam Long applicantId) {
        // TODO получение всех активных вакансий
        return ResponseEntity.ofNullable(vacancyService.getActiveVacancies(applicantId));
    }

    @GetMapping("category/{categoryId}")
    public ResponseEntity<List<VacancyDto>> getVacanciesByCategory(@PathVariable("categoryId") Long categoryId, @RequestParam Long applicantId) {
        // TODO получение вакансий по категории
        return ResponseEntity.ofNullable(vacancyService.getVacanciesByCategoryId(categoryId, applicantId));
    }

    @PostMapping
    public ResponseEntity<Long> createVacancy(@RequestBody VacancyDto vacancyDto, @RequestParam Long employerId) {
        // TODO создание вакансии
        return ResponseEntity.of(Optional.ofNullable(vacancyService.createVacancy(vacancyDto, employerId)));
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateVacancy(@PathVariable("id") Long vacancyId, @RequestBody VacancyDto vacancyDto, @RequestParam Long employerId) {
        // TODO обновление вакансии
        return ResponseEntity.of(Optional.ofNullable(vacancyService.updateVacancy(vacancyId, vacancyDto, employerId)));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteVacancy(@PathVariable("id") Long vacancyId, @RequestParam Long employerId) {
        // TODO удаление вакансии
        return vacancyService.deleteVacancy(vacancyId, employerId);
    }
}
