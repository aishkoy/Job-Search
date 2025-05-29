package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.ContactTypeDto;
import kg.attractor.job_search.entity.ContactType;
import kg.attractor.job_search.exception.nsee.ContactTypeNotFoundException;
import kg.attractor.job_search.mapper.ContactTypeMapper;
import kg.attractor.job_search.repository.ContactTypeRepository;
import kg.attractor.job_search.service.interfaces.ContactTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactTypeServiceImpl implements ContactTypeService {
    private final ContactTypeRepository contactTypeRepository;
    private final ContactTypeMapper contactTypeMapper;

    @Override
    public ContactTypeDto getContactTypeIfPresent(Long id) {
        ContactType contactType  = contactTypeRepository.findById(id)
                .orElseThrow(() -> new ContactTypeNotFoundException("Не существует типа контакта с таким id!"));
        log.info("Получен контакт {}", contactType.getName());
        return contactTypeMapper.toDto(contactType);
    }

    @Override
    public List<ContactTypeDto> getAllContactTypes(){
        List<ContactTypeDto> contactTypes =
                contactTypeRepository.findAll()
                        .stream()
                        .map(contactTypeMapper::toDto)
                        .toList();

        if(contactTypes.isEmpty()){
            throw new ContactTypeNotFoundException("Не было найдено никаких контактов!");
        }
        return contactTypes;
    }

}
