package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("vacancies")
public class VacancyController {
    private final VacancyService vacancyService;

    @GetMapping
    public ResponseEntity<List<VacancyDto>> getVacancies() {
        return ResponseEntity.ofNullable(vacancyService.getVacancies());
    }

    @GetMapping("{id}")
    public ResponseEntity<VacancyDto> getVacancyById(@PathVariable Long id) {
        return ResponseEntity.of(vacancyService.getVacancyById(id));
    }

    @PostMapping
    public ResponseEntity<Long> createVacancy(@RequestBody VacancyDto vacancyDto) {
        return ResponseEntity.ofNullable(vacancyService.createVacancy(vacancyDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateVacancy(@PathVariable("id") Long vacancyId, @RequestBody VacancyDto vacancyDto) {
        return ResponseEntity.ofNullable(vacancyService.updateVacancy(vacancyId, vacancyDto));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteVacancy(@PathVariable("id") Long vacancyId) {
        return vacancyService.deleteVacancy(vacancyId);
    }

    @GetMapping("active")
    public ResponseEntity<List<VacancyDto>> getActiveVacancies() {
        return ResponseEntity.ofNullable(vacancyService.getActiveVacancies());
    }

    @GetMapping("category/{categoryId}")
    public ResponseEntity<List<VacancyDto>> getVacanciesByCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ofNullable(vacancyService.getVacanciesByCategoryId(categoryId));
    }

    @GetMapping("category/by-name")
    public ResponseEntity<List<VacancyDto>> getVacanciesByCategoryByName(@RequestParam("name") String name) {
        return ResponseEntity.ofNullable(vacancyService.getVacanciesByCategoryName(name));
    }

    @GetMapping("applied/{userId}")
    public ResponseEntity<List<VacancyDto>> getVacanciesByApplicantId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ofNullable(vacancyService.getVacanciesAppliedByUserId(userId));
    }

    @GetMapping("employer/{id}")
    public ResponseEntity<List<VacancyDto>> getVacanciesByEmployerId(@PathVariable("id") Long id) {
        return ResponseEntity.ofNullable(vacancyService.getVacanciesByEmployerId(id));
    }
}
