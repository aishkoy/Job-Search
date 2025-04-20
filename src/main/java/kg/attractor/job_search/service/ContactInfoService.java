package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ContactInfoDto;
import org.springframework.http.HttpStatus;

public interface ContactInfoService {
    Long createContactInfo(ContactInfoDto contactInfoDto);

    Long updateContactInfo(ContactInfoDto contactInfoDto);

    ContactInfoDto getContactInfoById(Long id);

    HttpStatus deleteContactInfoById(Long id);
}
