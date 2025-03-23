package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("resumes")
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping
    public ResponseEntity<List<ResumeDto>> getResumes() {
        return ResponseEntity.ofNullable(resumeService.getResumes());
    }

    @PostMapping
    public ResponseEntity<Long> createResume(@RequestBody ResumeDto resumeDto) {
        return ResponseEntity.ofNullable(resumeService.createResume(resumeDto));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResumeDto> getResumeById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(resumeService.getResumeById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateResume(@PathVariable("id") Long resumeId, @RequestBody ResumeDto resumeDto) {
        return ResponseEntity.ofNullable(resumeService.updateResume(resumeId, resumeDto));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteResume(@PathVariable("id") Long resumeId) {
        return resumeService.deleteResume(resumeId);
    }

    @GetMapping("active")
    public ResponseEntity<List<ResumeDto>> getActiveResumes() {
        return ResponseEntity.ofNullable(resumeService.getActiveResumes());
    }

    @GetMapping("category/{categoryId}")
    public ResponseEntity<List<ResumeDto>> getResumesByCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ofNullable(resumeService.getResumesByCategoryId(categoryId));
    }

    @GetMapping("applicant/{id}")
    public ResponseEntity<List<ResumeDto>> getResumesByApplicantId(@PathVariable("id") Long applicantId) {
        return ResponseEntity.ofNullable(resumeService.getResumesByApplicantId(applicantId));
    }

    @GetMapping("applicant/by-name")
    public ResponseEntity<List<ResumeDto>> getResumesByApplicantName(@RequestParam String applicantName) {
        return ResponseEntity.ofNullable(resumeService.getResumesByApplicantName(applicantName));
    }
}
