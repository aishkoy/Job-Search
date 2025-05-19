package kg.attractor.job_search.controller.api;

import kg.attractor.job_search.dto.ResponseDto;
import kg.attractor.job_search.service.ResponseService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping("{vacancyId}")
    public ResponseEntity<Page<ResponseDto>> getResponsesByVacancyId(@PathVariable("vacancyId") Long vacancyId,
                                                                     @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                     @RequestParam(required = false, defaultValue = "3") Integer size) {
        return ResponseEntity.ofNullable(responseService.getResponsesByVacancyId(vacancyId, page, size));
    }

    @GetMapping("employers/{employerId}/count")
    public ResponseEntity<Integer> getResponsesByEmployerId(@PathVariable("employerId") Long employerId,
                                                            @RequestParam(required = false, defaultValue = "false") boolean isConfirmed){
        return ResponseEntity.ofNullable(responseService.getResponseCountByEmployerId(employerId, isConfirmed));
    }

    @GetMapping("applicants/{applicantId}")
    public ResponseEntity<Page<ResponseDto>> getResponsesByApplicantId(@PathVariable("applicantId") Long applicantId,
                                                                       @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                       @RequestParam(required = false, defaultValue = "3") Integer size) {
        return ResponseEntity.ofNullable(responseService.getResponsesByApplicantId(applicantId, page, size));
    }

    @GetMapping("applicants/{applicantId}/count")
    public ResponseEntity<Integer> getResponseCountByApplicantId(@PathVariable("applicantId") Long applicantId,
                                                                 @RequestParam(required = false, defaultValue = "false") boolean isConfirmed) {
        return ResponseEntity.ofNullable(responseService.getResponseCountByApplicantId(applicantId, isConfirmed));
    }
}
