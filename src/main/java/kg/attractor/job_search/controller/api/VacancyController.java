package kg.attractor.job_search.controller.api;

import jakarta.validation.Valid;
import kg.attractor.job_search.service.interfaces.UserService;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.service.interfaces.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vacancies")
public class VacancyController {
    private final VacancyService vacancyService;
    private final UserService adapter;

    @GetMapping
    public ResponseEntity<List<VacancyDto>> getVacancies() {
        return ResponseEntity.ofNullable(vacancyService.getVacancies());
    }

    @GetMapping("{id}")
    public ResponseEntity<VacancyDto> getVacancyById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(vacancyService.getVacancyDtoById(id));
    }

    @GetMapping("active")
    public ResponseEntity<List<VacancyDto>> getActiveVacancies() {
        return ResponseEntity.ofNullable(vacancyService.getActiveVacancies());
    }

    @GetMapping("categories/{categoryId}")
    public ResponseEntity<List<VacancyDto>> getVacanciesByCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ofNullable(vacancyService.getVacanciesByCategoryId(categoryId));
    }

    @GetMapping("categories/by-name")
    public ResponseEntity<List<VacancyDto>> getVacanciesByCategoryByName(@RequestParam("name") String name) {
        return ResponseEntity.ofNullable(vacancyService.getVacanciesByCategoryName(name));
    }

    @GetMapping("employers/{id}")
    public ResponseEntity<List<VacancyDto>> getVacanciesByEmployerId(@PathVariable("id") Long id) {
        return ResponseEntity.ofNullable(vacancyService.getVacanciesByEmployerId(id));
    }

    @PostMapping
    public ResponseEntity<Long> createVacancy(@RequestBody @Valid VacancyDto vacancyDto) {
        vacancyDto.setEmployer(adapter.getAuthUser());
        return ResponseEntity.ofNullable(vacancyService.createVacancy(vacancyDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateVacancy(@PathVariable("id") Long vacancyId, @RequestBody @Valid VacancyDto vacancyDto) {
        vacancyDto.setEmployer(adapter.getAuthUser());
        return ResponseEntity.ofNullable(vacancyService.updateVacancy(vacancyId, vacancyDto));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteVacancy(@PathVariable("id") Long vacancyId) {
        return vacancyService.deleteVacancy(vacancyId, adapter.getAuthId());
    }

    @GetMapping("applied/{userId}")
    public ResponseEntity<List<VacancyDto>> getVacanciesByApplicantId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ofNullable(vacancyService.getVacanciesAppliedByUserId(userId));
    }
}
