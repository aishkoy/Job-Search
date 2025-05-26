package kg.attractor.job_search.controller.api;

import jakarta.validation.Valid;
import kg.attractor.job_search.service.interfaces.UserService;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.service.interfaces.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService adapter;

    @GetMapping
    public ResponseEntity<List<ResumeDto>> getResumes() {
        return ResponseEntity.ofNullable(resumeService.getResumes());
    }

    @PostMapping
    public ResponseEntity<Long> createResume(@RequestBody @Valid ResumeDto resumeDto) {
        resumeDto.setApplicant(adapter.getAuthUser());
        return ResponseEntity.ofNullable(resumeService.createResume(resumeDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateResume(@PathVariable("id") Long resumeId, @RequestBody @Valid ResumeDto resumeDto) {
        resumeDto.setApplicant(adapter.getAuthUser());
        return ResponseEntity.ofNullable(resumeService.updateResume(resumeId, resumeDto));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteResume(@PathVariable("id") Long resumeId) {
        return resumeService.deleteResume(resumeId, adapter.getAuthId());
    }

    @GetMapping("applicants/{id}")
    public ResponseEntity<Page<ResumeDto>> getResumesByApplicantId(@PathVariable("id") Long userId,
                                                                   @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                   @RequestParam(required = false, defaultValue = "2") Integer size) {
        return ResponseEntity.ofNullable(resumeService.getResumesByApplicantId(userId, page, size));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResumeDto> getResumeById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(resumeService.getResumeDtoById(id, adapter.getAuthId()));
    }

    @GetMapping("active")
    public ResponseEntity<List<ResumeDto>> getActiveResumes() {
        return ResponseEntity.ofNullable(resumeService.getActiveResumes());
    }

    @GetMapping("categories/{categoryId}")
    public ResponseEntity<List<ResumeDto>> getResumesByCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ofNullable(resumeService.getResumesByCategoryId(categoryId));
    }

    @GetMapping("applicants/by-name")
    public ResponseEntity<List<ResumeDto>> getResumesByApplicantName(@RequestParam String applicantName) {
        return ResponseEntity.ofNullable(resumeService.getResumesByApplicantName(applicantName));
    }
}
