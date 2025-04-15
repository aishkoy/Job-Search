package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ResumeDao;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.dto.resume.ResumeFormDto;
import kg.attractor.job_search.exception.*;
import kg.attractor.job_search.exception.ApplicantNotFoundException;
import kg.attractor.job_search.mapper.ResumeMapper;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeDao resumeDao;
    private final ResumeMapper resumeMapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final EducationInfoService educationInfoService;
    private final WorkExperienceInfoService workExperienceInfoService;
    private final ContactInfoService contactInfoService;

    @Override
    public List<ResumeDto> getResumes() {
        List<ResumeDto> resumes = resumeDao.getResumes().stream().map(resumeMapper::toDto).toList();
        enrichResumesWithAdditionalData(resumes);
        validateResumesList(resumes, "Резюме не найдены");
        return resumes;
    }

    @Override
    public List<ResumeDto> getActiveResumes() {
        List<ResumeDto> resumes = resumeDao.getActiveResumes()
                .stream()
                .map(resumeMapper::toDto)
                .toList();
        enrichResumesWithAdditionalData(resumes);
        validateResumesList(resumes, "Активные резюме не найдены");
        return resumes;
    }

    @Override
    public List<ResumeDto> getResumesByApplicantId(Long userId) {
        userService.getApplicantById(userId);
        List<ResumeDto> resumes = resumeDao.getResumesByApplicantId(userId)
                .stream()
                .map(resumeMapper::toDto)
                .toList();
        enrichResumesWithAdditionalData(resumes);
        validateResumesList(resumes, "Резюме для пользователя с ID: " + userId + " не найдены");
        return resumes;
    }

    @Override
    public List<ResumeDto> getResumesByApplicantName(String name) {
        String userName = name.trim().toLowerCase();
        userName = StringUtils.capitalize(userName);

        userService.getUsersByName(userName);

        List<ResumeDto> resumes = resumeDao.getResumesByApplicantName(userName).stream()
                .map(resumeMapper::toDto)
                .toList();
        enrichResumesWithAdditionalData(resumes);
        validateResumesList(resumes, "Резюме для пользователя с именем: " + userName + " не найдены");
        return resumes;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Long createResume(ResumeFormDto resumeDto) {
        if (resumeDto.getApplicantId() == null) {
            throw new ApplicantNotFoundException();
        }

        categoryService.getCategoryIdIfPresent(resumeDto.getCategoryId());

        Long resumeId = resumeDao.createResume(resumeMapper.toEntity(resumeDto));

        processResumeItems(
                resumeDto.getEducations(),
                resumeId,
                EducationInfoDto::setResumeId,
                educationInfoService::createEducationInfo
        );

        processResumeItems(
                resumeDto.getWorkExperiences(),
                resumeId,
                WorkExperienceInfoDto::setResumeId,
                workExperienceInfoService::createWorkExperience
        );

        processResumeItems(
                resumeDto.getContacts(),
                resumeId,
                ContactInfoDto::setResumeId,
                contactInfoService::createContactInfo
        );

        log.info("Созданное резюме: {}", resumeDto);
        return resumeId;
    }

    @Override
    public ResumeDto getResumeById(Long resumeId, Long userId) {
        ResumeDto resume = getResumeById(resumeId);
        try {
            userService.getEmployerById(userId);
            return resume;
        } catch (EmployerNotFoundException ignore) {
            if (isResumeOwnedByApplicant(resumeId, userId)) {
                return resume;
            }
            throw new AccessDeniedException("Это не ваше резюме");
        }
    }

    @Override
    public ResumeDto getResumeById(Long resumeId) {
        Resume resume = resumeDao.getResumeById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Не существует резюме с таким id!"));

        ResumeDto resumeDto = resumeMapper.toDto(resume);
        enrichResumeWithAdditionalData(resumeDto);
        return resumeDto;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Long updateResume(Long resumeId, ResumeFormDto resumeDto) {
        getResumeById(resumeId);
        categoryService.getCategoryIdIfPresent(resumeDto.getCategoryId());
        if (!isResumeOwnedByApplicant(resumeId, resumeDto.getApplicantId())) {
            throw new AccessDeniedException("У вас нет прав на редактирование этого резюме");
        }

        Resume resume = resumeMapper.toEntity(resumeDto);
        resume.setId(resumeId);
        Long updatedResumeId = resumeDao.updateResume(resume);

        if (resumeDto.getEducations() != null) {
            for (EducationInfoDto education : resumeDto.getEducations()) {
                education.setResumeId(resumeId);
                if (education.getId() == null) {
                    educationInfoService.createEducationInfo(education);
                } else {
                    educationInfoService.updateEducationInfo(education.getId(), education);
                }
            }
        }

        if (resumeDto.getWorkExperiences() != null) {
            for (WorkExperienceInfoDto workExperience : resumeDto.getWorkExperiences()) {
                workExperience.setResumeId(resumeId);
                if (workExperience.getId() == null) {
                    workExperienceInfoService.createWorkExperience(workExperience);
                } else {
                    workExperienceInfoService.updateWorkExperienceInfo(workExperience.getId(), workExperience);
                }
            }
        }

        if (resumeDto.getContacts() != null) {
            for (ContactInfoDto contact : resumeDto.getContacts()) {
                contact.setResumeId(resumeId);
                if (contact.getId() == null) {
                    contactInfoService.createContactInfo(contact);
                } else {
                    contactInfoService.updateContactInfo(contact.getId(), contact);
                }
            }
        }

        log.info("Обновлено резюме: {}", resumeId);
        return updatedResumeId;
    }

    @Override
    public void addExperience(ResumeFormDto resumeForm) {
        if (resumeForm.getWorkExperiences() == null) {
            resumeForm.setWorkExperiences(new ArrayList<>());
        }
        resumeForm.getWorkExperiences().add(new WorkExperienceInfoDto());
    }

    @Override
    public void addEducation(ResumeFormDto resumeForm) {
        if (resumeForm.getEducations() == null) {
            resumeForm.setEducations(new ArrayList<>());
        }
        resumeForm.getEducations().add(new EducationInfoDto());
    }

    @Override
    public void addContact(ResumeFormDto resumeForm) {
        if (resumeForm.getContacts() == null) {
            resumeForm.setContacts(new ArrayList<>());
        }
        resumeForm.getContacts().add(new ContactInfoDto());
    }

    @Override
    public void deleteEducation(Long educationId) {
        educationInfoService.deleteEducationInfo(educationId);
        log.info("Удалено образование с ID: {}", educationId);
    }

    @Override
    public void deleteWorkExperience(Long workExperienceId) {
        workExperienceInfoService.deleteWorkExperienceInfo(workExperienceId);
        log.info("Удален опыт работы с ID: {}", workExperienceId);
    }

    @Override
    public void deleteContact(Long contactId) {
        contactInfoService.deleteContactInfoById(contactId);
        log.info("Удалена контактная информация с ID: {}", contactId);
    }

    @Override
    public HttpStatus deleteResume(Long resumeId, Long userId) {
        getResumeById(resumeId);
        if (!isResumeOwnedByApplicant(resumeId, userId)) {
            throw new AccessDeniedException("У вас нет прав на удаление этого резюме!");
        }
        resumeDao.deleteResume(resumeId);
        log.info("Удалено резюме: {}", resumeId);
        return HttpStatus.OK;
    }

    @Override
    public List<ResumeDto> getResumesByCategoryId(Long categoryId) {
        Long category = categoryService.getCategoryIdIfPresent(categoryId);

        List<ResumeDto> resumes = resumeDao.getResumesByCategoryId(category)
                .stream()
                .map(resumeMapper::toDto)
                .toList();
        enrichResumesWithAdditionalData(resumes);
        validateResumesList(resumes, "Резюме для категории с ID: " + categoryId + " не найдены");
        return resumes;
    }

    @Override
    public Long changeActiveStatus(Long resumeId, Long userId) {
        ResumeDto resume = getResumeById(resumeId);
        if (!isResumeOwnedByApplicant(resumeId, userId)) {
            throw new AccessDeniedException("У вас нет прав на изменение статуса этого резюме!");
        }

        Boolean status = resume.getIsActive().equals(Boolean.FALSE) ? Boolean.TRUE : Boolean.FALSE;

        resumeDao.updateResumeActiveStatus(resumeId, status);
        return resumeId;
    }

    @Override
    public ResumeFormDto convertToFormDto(ResumeDto dto){
        return resumeMapper.toFormDto(dto);
    }

    private <T> void processResumeItems(Collection<T> items, Long resumeId,
                                        ObjLongConsumer<T> setResumeIdFunc,
                                        Function<T, ?> createFunction) {
        if (items != null && !items.isEmpty()) {
            for (T item : items) {
                setResumeIdFunc.accept(item, resumeId);
                createFunction.apply(item);
            }
        }
    }

    public boolean isResumeOwnedByApplicant(Long resumeId, Long applicantId) {
        return resumeDao.isResumeOwnedByApplicant(resumeId, applicantId);
    }

    private void enrichResumesWithAdditionalData(List<ResumeDto> resumes) {
        for (ResumeDto resume : resumes) {
            enrichResumeWithAdditionalData(resume);
        }
    }

    private void enrichResumeWithAdditionalData(ResumeDto resume) {
        resume.setWorkExperiences(workExperienceInfoService.getWorkExperienceInfoByResumeId(resume.getId()));
        resume.setEducations(educationInfoService.getEducationInfoByResumeId(resume.getId()));
        resume.setContacts(contactInfoService.getContactInfoByResumeId(resume.getId()));
        resume.setApplicantName(userService.getUserName(resume.getApplicantId()));
    }

    private void validateResumesList(List<ResumeDto> resumes, String errorMessage) {
        if (resumes.isEmpty()) {
            log.warn(errorMessage);
            throw new ResumeNotFoundException(errorMessage);
        }
        log.info("Получено {} резюме", resumes.size());
    }
}
