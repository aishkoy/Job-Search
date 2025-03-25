package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ContactInfoDao;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.exceptions.ContactInfoNotFoundException;
import kg.attractor.job_search.mapper.ContactInfoMapper;
import kg.attractor.job_search.models.ContactInfo;
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

    @Override
    public List<ContactInfoDto> getContactInfoByResumeId(Long resumeId) {
        List<ContactInfoDto> contacts = contactInfoDao.getContactInfoByResumeId(resumeId).stream().map(ContactInfoMapper::toDto).toList();
        log.info("Retrieved {} contacts", contacts.size());
        return contacts;
    }

    @Override
    public Long createContactInfo(ContactInfoDto contactInfoDto) {
        contactTypeService.getContactTypeIdIfPresent(contactInfoDto.getTypeId());
        Long id = contactInfoDao.createContactInfo(ContactInfoMapper.toEntity(contactInfoDto));
        log.info("Created new contact: {}", contactInfoDto);
        return id;
    }

    @Override
    public Long updateContactInfo(Long id, ContactInfoDto contactInfoDto) {
        if (!contactInfoDto.getResumeId().equals(id)) {
            throw new ContactInfoNotFoundException();
        }
        getContactInfoById(id);
        log.info("Updating contact: {}", contactInfoDto);
        return contactInfoDao.updateContactInfo(ContactInfoMapper.toEntity(contactInfoDto));
    }

    @Override
    public ContactInfoDto getContactInfoById(Long id) {
        ContactInfo contactInfo = contactInfoDao.getContactInfoById(id).orElseThrow(ContactInfoNotFoundException::new);
        log.info("Retrieved contact: {}", contactInfo);
        return ContactInfoMapper.toDto(contactInfo);
    }

    @Override
    public HttpStatus deleteContactInfoById(Long id) {
        getContactInfoById(id);
        contactInfoDao.deleteContactInfo(id);
        log.info("Deleted contact: {}", id);
        return HttpStatus.OK;
    }
}
