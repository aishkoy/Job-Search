package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.service.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("work-experience")
public class WorkExperienceInfoController {
    private final WorkExperienceInfoService workExperienceInfoService;

    @GetMapping("resume/{resumeId}")
    public ResponseEntity<List<WorkExperienceInfoDto>> getWorkExperienceInfoByResumeId(@PathVariable Long resumeId) {
        return ResponseEntity.ofNullable(workExperienceInfoService.getWorkExperienceInfoByResumeId(resumeId));
    }

    @PostMapping()
    public ResponseEntity<Long> createWorkExperience(@RequestBody WorkExperienceInfoDto workExperienceInfoDto) {
        return ResponseEntity.ofNullable(workExperienceInfoService.createWorkExperience(workExperienceInfoDto));
    }

    @GetMapping("{id}")
    public ResponseEntity<WorkExperienceInfoDto> getWorkExperienceInfoById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(workExperienceInfoService.getWorkExperienceById(id));
    }

    @PutMapping("{id}")
    public  ResponseEntity<Long> updateWorkExperience(@RequestBody WorkExperienceInfoDto workExperienceInfoDto, @PathVariable Long id) {
        return ResponseEntity.ofNullable(workExperienceInfoService.updateWorkExperienceInfo(id, workExperienceInfoDto));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteWorkExperience(@PathVariable Long id) {
        return workExperienceInfoService.deleteWorkExperienceInfo(id);
    }
}
