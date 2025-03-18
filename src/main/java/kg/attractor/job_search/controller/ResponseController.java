package kg.attractor.job_search.controller;

import kg.attractor.job_search.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("responses")
public class ResponseController {
    private final ResponseService responseService;

    @PostMapping("{vacancyId}") // /vacancies/1?applicantId=9
    public HttpStatus applyVacancy(@PathVariable("vacancyId") Long vacancyId) {
        // TODO откликнуться на вакансию
        return responseService.applyVacancy(vacancyId);
    }


}
