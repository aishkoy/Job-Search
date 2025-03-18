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
    public ResponseEntity<List<VacancyDto>> getVacancies() {
        // TODO получение всех активных вакансий
        return ResponseEntity.ofNullable(vacancyService.getActiveVacancies());
    }

    @GetMapping("category/{categoryId}")
    public ResponseEntity<List<VacancyDto>> getVacanciesByCategory(@PathVariable("categoryId") Long categoryId) {
        // TODO получение вакансий по категории
        return ResponseEntity.ofNullable(vacancyService.getVacanciesByCategoryId(categoryId));
    }

    @PostMapping
    public ResponseEntity<Long> createVacancy(@RequestBody VacancyDto vacancyDto) {
        // TODO создание вакансии
        return ResponseEntity.of(Optional.ofNullable(vacancyService.createVacancy(vacancyDto)));
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateVacancy(@PathVariable("id") Long vacancyId, @RequestBody VacancyDto vacancyDto) {
        // TODO обновление вакансии
        return ResponseEntity.of(Optional.ofNullable(vacancyService.updateVacancy(vacancyId, vacancyDto)));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteVacancy(@PathVariable("id") Long vacancyId) {
        // TODO удаление вакансии
        return vacancyService.deleteVacancy(vacancyId);
    }
}
