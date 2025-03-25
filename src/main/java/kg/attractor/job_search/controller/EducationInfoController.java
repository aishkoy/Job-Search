package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.service.EducationInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("education-info")
public class EducationInfoController {
    private final EducationInfoService educationInfoService;

    @GetMapping("resume/{resumeId}")
    public ResponseEntity<List<EducationInfoDto>> getEducationInfoByResumeId(@PathVariable Long resumeId) {
        return ResponseEntity.ofNullable(educationInfoService.getEducationInfoByResumeId(resumeId));
    }

    @PostMapping()
    public ResponseEntity<Long> createEducationInfo(@RequestBody EducationInfoDto educationInfoDto) {
        return ResponseEntity.ofNullable(educationInfoService.createEducationInfo(educationInfoDto));
    }

    @GetMapping("{id}")
    public ResponseEntity<EducationInfoDto> getEducationInfoById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(educationInfoService.getEducationInfoById(id));
    }

    @PutMapping("{id}")
    public  ResponseEntity<Long> updateEducationInfo(@RequestBody EducationInfoDto educationInfoDto, @PathVariable Long id) {
        return ResponseEntity.ofNullable(educationInfoService.updateEducationInfo(id, educationInfoDto));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteEducationInfo(@PathVariable Long id) {
        return educationInfoService.deleteEducationInfo(id);
    }
}
