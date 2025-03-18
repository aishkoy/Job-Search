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
    public ResponseEntity<List<ResumeDto>> getResumes(@RequestParam("employerId") Long employerId) {
        //TODO получение всех резюме для работодателя
        return ResponseEntity.ofNullable(resumeService.getResumes(employerId));
    }

    @GetMapping("category/{categoryId}")
    public ResponseEntity<List<ResumeDto>> getResumesByCategory(@PathVariable("categoryId") Long categoryId, @RequestParam("employerId") Long employerId) {
        //TODO получение всех резюме по категории для работодателя
        return ResponseEntity.ofNullable(resumeService.getResumesByCategoryId(categoryId, employerId));
    }

    @PostMapping
    public ResponseEntity<Long> createResume(@RequestBody ResumeDto resumeDto, @RequestParam("applicantId") Long applicantId) {
        //TODO создание резюме для соискателя
        return ResponseEntity.ofNullable(resumeService.createResume(resumeDto, applicantId));
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateResume(@PathVariable("id") Long resumeId, @RequestBody ResumeDto resumeDto, @RequestParam("applicantId") Long applicantId) {
        //TODO обновление резюме для соискателя
        return ResponseEntity.ofNullable(resumeService.updateResume(resumeId, resumeDto, applicantId));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteResume(@PathVariable("id") Long resumeId, @RequestParam("applicantId") Long applicantId) {
        //TODO удаление резюме для соискателя
        return resumeService.deleteResume(resumeId, applicantId);
    }
}
