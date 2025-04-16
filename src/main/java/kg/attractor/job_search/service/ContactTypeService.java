package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ContactTypeDto;

public interface ContactTypeService {
    ContactTypeDto getContactTypeIfPresent(Long id);
}
