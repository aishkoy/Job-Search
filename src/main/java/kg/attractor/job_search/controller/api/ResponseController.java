package kg.attractor.job_search.controller.api;

import kg.attractor.job_search.dto.ResponseDto;
import kg.attractor.job_search.service.interfaces.ResponseService;
import kg.attractor.job_search.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/responses")
public class ResponseController {
    private final ResponseService responseService;
    private final UserService userService;

    @GetMapping("{responseId}")
    public ResponseEntity<ResponseDto> getResponseById(@PathVariable Long responseId) {
        return ResponseEntity.ok(responseService.getResponseById(responseId));
    }

    @PreAuthorize("hasRole('EMPLOYER') and @responseService.isChatParticipant(#responseId, authentication.principal.userId)")
    @PutMapping("{responseId}")
    public ResponseEntity<ResponseDto> approveOrDismissResponse(@PathVariable Long responseId) {
        return ResponseEntity.ok(responseService.approveOrDismissResponse(responseId));
    }

    @PostMapping("vacancies/{vacancyId}")
    public ResponseEntity<Long> applyVacancy(@PathVariable("vacancyId") Long vacancyId, @RequestParam("resumeId") Long resumeId) {
        return ResponseEntity.ofNullable(responseService.applyVacancy(vacancyId, resumeId, userService.getAuthId()));
    }

    @GetMapping("vacancies/{vacancyId}/applicants/{applicantId}")
    public ResponseEntity<Boolean> isApplicantApplied(@PathVariable("vacancyId") Long vacancyId, @PathVariable("applicantId") Long applicantId) {
        return ResponseEntity.ofNullable(responseService.isApplicantApplied(vacancyId, applicantId));
    }

    @GetMapping("vacancies/{vacancyId}")
    public ResponseEntity<Page<ResponseDto>> getResponsesByVacancyId(@PathVariable("vacancyId") Long vacancyId,
                                                                     @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                     @RequestParam(required = false, defaultValue = "3") Integer size) {
        return ResponseEntity.ofNullable(responseService.getResponsesByVacancyId(vacancyId, page, size));
    }

    @GetMapping("resumes/{resumeId}")
    public ResponseEntity<Page<ResponseDto>> getResponsesByResumeId(@PathVariable("resumeId") Long resumeId,
                                                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                    @RequestParam(required = false, defaultValue = "3") Integer size) {
        return ResponseEntity.ofNullable(responseService.getResponsesByResumeId(resumeId, page, size));
    }

    @GetMapping("employers/{employerId}")
    public ResponseEntity<Page<ResponseDto>> getResponsesByEmployerId(@PathVariable("employerId") Long employerId,
                                                                      @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                      @RequestParam(required = false, defaultValue = "3") Integer size,
                                                                      @RequestParam(required = false, defaultValue = "false") boolean isConfirmed) {
        return ResponseEntity.ofNullable(responseService.getResponsesByEmployerId(employerId, page, size, isConfirmed));
    }


    @GetMapping("applicants/{applicantId}")
    public ResponseEntity<Page<ResponseDto>> getResponsesByApplicantId(@PathVariable("applicantId") Long applicantId,
                                                                       @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                       @RequestParam(required = false, defaultValue = "3") Integer size,
                                                                       @RequestParam(required = false, defaultValue = "false") boolean isConfirmed) {
        return ResponseEntity.ofNullable(responseService.getResponsesByApplicantId(applicantId, page, size, isConfirmed));
    }

    @GetMapping("employers/{employerId}/count")
    public ResponseEntity<Integer> getResponsesCountByEmployerId(@PathVariable("employerId") Long employerId,
                                                                 @RequestParam(required = false, defaultValue = "false") boolean isConfirmed) {
        return ResponseEntity.ofNullable(responseService.getResponseCountByEmployerId(employerId, isConfirmed));
    }

    @GetMapping("applicants/{applicantId}/count")
    public ResponseEntity<Integer> getResponseCountByApplicantId(@PathVariable("applicantId") Long applicantId,
                                                                 @RequestParam(required = false, defaultValue = "false") boolean isConfirmed) {
        return ResponseEntity.ofNullable(responseService.getResponseCountByApplicantId(applicantId, isConfirmed));
    }
}
