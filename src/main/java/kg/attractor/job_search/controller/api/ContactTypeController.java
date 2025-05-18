package kg.attractor.job_search.controller.api;

import kg.attractor.job_search.dto.ContactTypeDto;
import kg.attractor.job_search.service.ContactTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/contact-types")
@RequiredArgsConstructor
public class ContactTypeController {
    private final ContactTypeService contactTypeService;

    @GetMapping
    public ResponseEntity<List<ContactTypeDto>> getContactTypes() {
        return ResponseEntity.ofNullable(contactTypeService.getAllContactTypes());
    }
}
