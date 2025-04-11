package kg.attractor.job_search.controller.api;

import kg.attractor.job_search.service.ResponseService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/responses")
public class ResponseController {
    private final ResponseService responseService;
    private final UserService userService;

    @PostMapping("{vacancyId}")
    public ResponseEntity<Long> applyVacancy(@PathVariable("vacancyId") Long vacancyId, @RequestParam("resumeId") Long resumeId) {
        return ResponseEntity.ofNullable(responseService.applyVacancy(vacancyId, resumeId, userService.getAuthId()));
    }
}
