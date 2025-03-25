package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.service.ContactInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("contact-info")
public class ContactInfoController {
    private final ContactInfoService contactInfoService;

    @GetMapping("resume/{resumeId}")
    public ResponseEntity<List<ContactInfoDto>> getContactInfoByResumeId(@PathVariable Long resumeId) {
        return ResponseEntity.ofNullable(contactInfoService.getContactInfoByResumeId(resumeId));
    }

    @PostMapping()
    public ResponseEntity<Long> createContactInfo(@RequestBody ContactInfoDto contactInfoDto) {
        return ResponseEntity.ofNullable(contactInfoService.createContactInfo(contactInfoDto));
    }

    @GetMapping("{id}")
    public ResponseEntity<ContactInfoDto> getContactInfoById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(contactInfoService.getContactInfoById(id));
    }

    @PutMapping("{id}")
    public  ResponseEntity<Long> updateContactInfo(@RequestBody ContactInfoDto contactInfoDto, @PathVariable Long id) {
        return ResponseEntity.ofNullable(contactInfoService.updateContactInfo(id, contactInfoDto));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteContactInfo(@PathVariable Long id) {
        return contactInfoService.deleteContactInfoById(id);
    }
}
