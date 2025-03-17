package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("responses")
public class ResponseController {
    private final ResponseService responseService;

    @PostMapping("{vacancyId}") // /vacancies/1?applicantId=9
    public HttpStatus applyVacancy(@PathVariable("vacancyId") Long vacancyId, @RequestParam("applicantId") Long applicantId) {
        // TODO откликнуться на вакансию
        return responseService.applyVacancy(vacancyId, applicantId);
    }

    @GetMapping("{vacancyId}")
    public ResponseEntity<List<UserDto>> getApplications(@PathVariable("vacancyId") Long vacancyId, @RequestParam("employerId") Long employerId) {
        // TODO получение всех пользователей откликнувшихся на вакансию
        return ResponseEntity.ofNullable(responseService.getApplicationsByVacancyId(vacancyId, employerId));
    }
}
