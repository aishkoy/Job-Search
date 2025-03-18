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
        //TODO получение всех резюме для работодателя
        return ResponseEntity.ofNullable(resumeService.getResumes());
    }

    @GetMapping("category/{categoryId}")
    public ResponseEntity<List<ResumeDto>> getResumesByCategory(@PathVariable("categoryId") Long categoryId) {
        //TODO получение всех резюме по категории для работодателя
        return ResponseEntity.ofNullable(resumeService.getResumesByCategoryId(categoryId));
    }

    @PostMapping
    public ResponseEntity<Long> createResume(@RequestBody ResumeDto resumeDto) {
        //TODO создание резюме для соискателя
        return ResponseEntity.ofNullable(resumeService.createResume(resumeDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateResume(@PathVariable("id") Long resumeId, @RequestBody ResumeDto resumeDto) {
        //TODO обновление резюме для соискателя
        return ResponseEntity.ofNullable(resumeService.updateResume(resumeId, resumeDto));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteResume(@PathVariable("id") Long resumeId) {
        //TODO удаление резюме для соискателя
        return resumeService.deleteResume(resumeId);
    }
}
