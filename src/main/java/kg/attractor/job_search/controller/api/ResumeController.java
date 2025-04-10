package kg.attractor.job_search.controller.api;

import jakarta.validation.Valid;
import kg.attractor.job_search.util.AuthAdapter;
import kg.attractor.job_search.dto.resume.ResumeFormDto;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    private final AuthAdapter adapter;

    @GetMapping
    public ResponseEntity<List<ResumeDto>> getResumes() {
        return ResponseEntity.ofNullable(resumeService.getResumes());
    }

    @PostMapping
    public ResponseEntity<Long> createResume(@RequestBody @Valid ResumeFormDto resumeDto) {
        resumeDto.setApplicantId(adapter.getAuthId());
        return ResponseEntity.ofNullable(resumeService.createResume(resumeDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateResume(@PathVariable("id") Long resumeId, @RequestBody @Valid ResumeFormDto resumeDto) {
        resumeDto.setApplicantId(adapter.getAuthId());
        return ResponseEntity.ofNullable(resumeService.updateResume(resumeId, resumeDto));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteResume(@PathVariable("id") Long resumeId) {
        return resumeService.deleteResume(resumeId, adapter.getAuthId());
    }

    @PutMapping("{id}/active")
    public ResponseEntity<Long> changeActiveStatus(@PathVariable("id") Long resumeId) {
        return ResponseEntity.ofNullable(resumeService.changeActiveStatus(resumeId, adapter.getAuthId()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResumeDto> getResumeById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(resumeService.getResumeById(id, adapter.getAuthId()));
    }

    @GetMapping("active")
    public ResponseEntity<List<ResumeDto>> getActiveResumes() {
        return ResponseEntity.ofNullable(resumeService.getActiveResumes());
    }

    @GetMapping("categories/{categoryId}")
    public ResponseEntity<List<ResumeDto>> getResumesByCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ofNullable(resumeService.getResumesByCategoryId(categoryId));
    }

    @GetMapping("applicants/{id}")
    public ResponseEntity<List<ResumeDto>> getResumesByApplicantId(@PathVariable("id") Long applicantId) {
        return ResponseEntity.ofNullable(resumeService.getResumesByApplicantId(applicantId));
    }

    @GetMapping("applicants/by-name")
    public ResponseEntity<List<ResumeDto>> getResumesByApplicantName(@RequestParam String applicantName) {
        return ResponseEntity.ofNullable(resumeService.getResumesByApplicantName(applicantName));
    }
}
