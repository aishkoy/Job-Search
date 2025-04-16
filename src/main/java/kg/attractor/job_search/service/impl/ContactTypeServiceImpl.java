package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.ContactTypeDto;
import kg.attractor.job_search.entity.ContactType;
import kg.attractor.job_search.exception.ContactTypeNotFoundException;
import kg.attractor.job_search.mapper.ContactTypeMapper;
import kg.attractor.job_search.repository.ContactTypeRepository;
import kg.attractor.job_search.service.ContactTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactTypeServiceImpl implements ContactTypeService {
    private final ContactTypeRepository contactTypeRepository;
    private final ContactTypeMapper contactTypeMapper;

    @Override
    public ContactTypeDto getContactTypeIfPresent(Long id) {
        ContactType contactType  = contactTypeRepository.findById(id)
                .orElseThrow(() -> new ContactTypeNotFoundException("Не существует типа контакта с таким id!"));
        return contactTypeMapper.toDto(contactType);
    }
}
