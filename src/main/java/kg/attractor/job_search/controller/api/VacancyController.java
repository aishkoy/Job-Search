package kg.attractor.job_search.controller.api;

import jakarta.validation.Valid;
import kg.attractor.job_search.service.interfaces.UserService;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.service.interfaces.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vacancies")
public class VacancyController {
    private final VacancyService vacancyService;
    private final UserService adapter;

    @GetMapping
    public ResponseEntity<Page<VacancyDto>> vacancies(@RequestParam(required = false) String query,
                                                      @RequestParam(required = false, defaultValue = "1") int page,
                                                      @RequestParam(required = false, defaultValue = "5") int size,
                                                      @RequestParam(required = false) Long categoryId,
                                                      @RequestParam(required = false, defaultValue = "updatedAt") String sortBy,
                                                      @RequestParam(required = false, defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ofNullable(vacancyService.getActiveVacanciesPage(
                query, page, size, categoryId, sortBy, sortDirection));
    }

    @GetMapping("{id}")
    public ResponseEntity<VacancyDto> getVacancyById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(vacancyService.getVacancyDtoById(id));
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

    @GetMapping("employers/{id}")
    public ResponseEntity<Page<VacancyDto>> getVacanciesByEmployerId(@PathVariable("id") Long userId,
                                                                     @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                     @RequestParam(required = false, defaultValue = "2") Integer size) {
        return ResponseEntity.ofNullable(vacancyService.getVacanciesByEmployerId(userId, page, size));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteVacancy(@PathVariable("id") Long vacancyId) {
        return vacancyService.deleteVacancy(vacancyId, adapter.getAuthId());
    }
}
