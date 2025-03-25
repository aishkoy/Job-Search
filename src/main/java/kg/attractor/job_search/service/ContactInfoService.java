package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ContactInfoDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface ContactInfoService {
    List<ContactInfoDto> getContactInfoByResumeId(Long resumeId);

    Long createContactInfo(ContactInfoDto contactInfoDto);

    Long updateContactInfo(Long id, ContactInfoDto contactInfoDto);

    ContactInfoDto getContactInfoById(Long id);

    HttpStatus deleteContactInfoById(Long id);
}
