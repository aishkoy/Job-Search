package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.entity.Resume;
import kg.attractor.job_search.exception.ContactInfoNotFoundException;
import kg.attractor.job_search.mapper.ContactInfoMapper;
import kg.attractor.job_search.entity.ContactInfo;
import kg.attractor.job_search.repository.ContactInfoRepository;
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
    private final ContactInfoRepository contactInfoRepository;
    private final ContactTypeService contactTypeService;
    private final ContactInfoMapper contactInfoMapper;

    @Override
    public Long createContactInfo(ContactInfoDto contactInfoDto) {
        contactTypeService.getContactTypeIfPresent(contactInfoDto.getContactType().getId());
        ContactInfo contactInfo = contactInfoMapper.toEntity(contactInfoDto);
        contactInfo.setResume(Resume.builder().id(contactInfoDto.getResumeId()).build());
        contactInfoRepository.save(contactInfo);
        log.info("Создан новый контакт: {}", contactInfoDto);
        return contactInfo.getId();
    }

    @Override
    public Long updateContactInfo(ContactInfoDto contactInfoDto) {
        ContactInfo contactInfo = contactInfoMapper.toEntity(contactInfoDto);
        contactInfo.setResume(Resume.builder().id(contactInfoDto.getResumeId()).build());
        log.info("Обновление контакта: {}", contactInfoDto);
        contactInfoRepository.save(contactInfo);
        return contactInfo.getId();
    }

    @Override
    public ContactInfoDto getContactInfoById(Long id) {
        ContactInfo contactInfo =
                contactInfoRepository.findById(id)
                        .orElseThrow(ContactInfoNotFoundException::new);
        log.info("Полученный контакт: {}", contactInfo);
        return contactInfoMapper.toDto(contactInfo);
    }

    @Override
    public HttpStatus deleteContactInfoById(Long id) {
        getContactInfoById(id);
        contactInfoRepository.deleteById(id);
        log.info("Удаленный контакт: {}", id);
        return HttpStatus.OK;
    }
}
