package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ContactInfoDao;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.exception.ContactInfoNotFoundException;
import kg.attractor.job_search.mapper.ContactInfoMapper;
import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.service.ContactInfoService;
import kg.attractor.job_search.service.ContactTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {
    private final ContactInfoDao contactInfoDao;
    private final ContactTypeService contactTypeService;
    private final ContactInfoMapper contactInfoMapper;

    @Override
    public List<ContactInfoDto> getContactInfoByResumeId(Long resumeId) {
        List<ContactInfoDto> contacts = contactInfoDao.getContactInfoByResumeId(resumeId).stream().map(contactInfoMapper::toDto).toList();
        log.info("Получено {} контактов", contacts.size());
        return contacts;
    }

    @Override
    public Long createContactInfo(ContactInfoDto contactInfoDto) {
        contactTypeService.getContactTypeIdIfPresent(contactInfoDto.getTypeId());
        Long id = contactInfoDao.createContactInfo(contactInfoMapper.toEntity(contactInfoDto));
        log.info("Создан новый контакт: {}", contactInfoDto);
        return id;
    }

    @Override
    public Long updateContactInfo(Long id, ContactInfoDto contactInfoDto) {
        if (!contactInfoDto.getResumeId().equals(id)) {
            throw new ContactInfoNotFoundException();
        }
        getContactInfoById(id);
        log.info("Обновление контакта: {}", contactInfoDto);
        return contactInfoDao.updateContactInfo(contactInfoMapper.toEntity(contactInfoDto));
    }

    @Override
    public ContactInfoDto getContactInfoById(Long id) {
        ContactInfo contactInfo = contactInfoDao.getContactInfoById(id).orElseThrow(ContactInfoNotFoundException::new);
        log.info("Полученный контакт: {}", contactInfo);
        return contactInfoMapper.toDto(contactInfo);
    }

    @Override
    public HttpStatus deleteContactInfoById(Long id) {
        getContactInfoById(id);
        contactInfoDao.deleteContactInfo(id);
        log.info("Удаленный контакт: {}", id);
        return HttpStatus.OK;
    }
}
