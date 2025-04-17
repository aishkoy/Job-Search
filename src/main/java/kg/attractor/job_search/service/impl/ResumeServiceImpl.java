package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.dto.resume.ResumeFormDto;
import kg.attractor.job_search.exception.*;
import kg.attractor.job_search.exception.ApplicantNotFoundException;
import kg.attractor.job_search.mapper.ResumeMapper;
import kg.attractor.job_search.entity.Resume;
import kg.attractor.job_search.repository.ResumeRepository;
import kg.attractor.job_search.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final EducationInfoService educationInfoService;
    private final WorkExperienceInfoService workExperienceInfoService;
    private final ContactInfoService contactInfoService;

    @Override
    public List<ResumeDto> getResumes() {
        List<ResumeDto> resumes =
                resumeRepository.findAll()
                        .stream()
                        .map(resumeMapper::toDto)
                        .toList();
        enrichResumesWithAdditionalData(resumes);
        validateResumesList(resumes, "Резюме не найдены");
        return resumes;
    }

    @Override
    public List<ResumeDto> getActiveResumes() {
        List<ResumeDto> resumes =
                resumeRepository.findAllByIsActiveTrue()
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
        List<ResumeDto> resumes =
                resumeRepository.findAllByApplicantId(userId)
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

        List<ResumeDto> resumes =
                resumeRepository.findAllByApplicantName(userName)
                        .stream()
                        .map(resumeMapper::toDto)
                        .toList();
        enrichResumesWithAdditionalData(resumes);
        validateResumesList(resumes, "Резюме для пользователя с именем: " + userName + " не найдены");
        return resumes;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Long createResume(ResumeFormDto resumeDto) {
        if (resumeDto.getApplicant() == null) {
            throw new ApplicantNotFoundException();
        }

        categoryService.getCategoryIfPresent(resumeDto.getCategory().getId());
        ResumeDto dto = resumeMapper.toDto(resumeDto);
        dto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        dto.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        Resume resume = resumeMapper.toEntity(dto);
        resumeRepository.save(resume);

        processResumeItems(
                resumeDto.getEducations(),
                dto,
                EducationInfoDto::setResume,
                educationInfoService::createEducationInfo
        );

        processResumeItems(
                resumeDto.getWorkExperiences(),
                dto,
                WorkExperienceInfoDto::setResume,
                workExperienceInfoService::createWorkExperience
        );

        processResumeItems(
                resumeDto.getContacts(),
                dto,
                ContactInfoDto::setResume,
                contactInfoService::createContactInfo
        );

        log.info("Созданное резюме: {}", resumeDto);
        return resume.getId();
    }

    @Override
    public Resume getResumeById(Long resumeId, Long userId) {
        Resume resume = getResumeById(resumeId);
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
    public Resume getResumeById(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Не существует резюме с таким id!"));

        log.info("Получено резюме {}", resumeId);
        return resume;
    }


    @Override
    public ResumeDto getResumeDtoById(Long resumeId, Long userId) {
        ResumeDto resume = getResumeDtoById(resumeId);
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
    public ResumeDto getResumeDtoById(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Не существует резюме с таким id!"));

        ResumeDto resumeDto = resumeMapper.toDto(resume);
        enrichResumeWithAdditionalData(resumeDto);
        return resumeDto;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Long updateResume(Long resumeId, ResumeFormDto resumeForm) {
        ResumeDto dto = getResumeDtoById(resumeId);
        categoryService.getCategoryIfPresent(resumeForm.getCategory().getId());

        if (!isResumeOwnedByApplicant(resumeId, resumeForm.getApplicant().getId())) {
            throw new AccessDeniedException("У вас нет прав на редактирование этого резюме");
        }

        dto.setName(resumeForm.getName());
        dto.setCategory(resumeForm.getCategory());
        dto.setSalary(resumeForm.getSalary());
        dto.setIsActive(resumeForm.getIsActive());
        dto.setContacts(resumeForm.getContacts());
        dto.setWorkExperiences(resumeForm.getWorkExperiences());
        dto.setEducations(resumeForm.getEducations());
        dto.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        Resume resume = resumeMapper.toEntity(dto);
        resumeRepository.save(resume);

        if (resumeForm.getEducations() != null) {
            for (EducationInfoDto education : resumeForm.getEducations()) {
                education.setResume(dto);
                if (education.getId() == null) {
                    educationInfoService.createEducationInfo(education);
                } else {
                    educationInfoService.updateEducationInfo(education.getId(), education);
                }
            }
        }

        if (resumeForm.getWorkExperiences() != null) {
            for (WorkExperienceInfoDto workExperience : resumeForm.getWorkExperiences()) {
                workExperience.setResume(dto);
                if (workExperience.getId() == null) {
                    workExperienceInfoService.createWorkExperience(workExperience);
                } else {
                    workExperienceInfoService.updateWorkExperienceInfo(workExperience.getId(), workExperience);
                }
            }
        }

        if (resumeForm.getContacts() != null) {
            for (ContactInfoDto contact : resumeForm.getContacts()) {
                contact.setResume(dto);
                if (contact.getId() == null) {
                    contactInfoService.createContactInfo(contact);
                } else {
                    contactInfoService.updateContactInfo(contact.getId(), contact);
                }
            }
        }

        log.info("Обновлено резюме: {}", resumeId);
        return resumeId;
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
    public HttpStatus deleteResume(Long resumeId, Long userId) {
        getResumeDtoById(resumeId);
        if (!isResumeOwnedByApplicant(resumeId, userId)) {
            throw new AccessDeniedException("У вас нет прав на удаление этого резюме!");
        }
        resumeRepository.deleteById(resumeId);
        log.info("Удалено резюме: {}", resumeId);
        return HttpStatus.OK;
    }

    @Override
    public List<ResumeDto> getResumesByCategoryId(Long categoryId) {
        categoryService.getCategoryIfPresent(categoryId);

        List<ResumeDto> resumes = resumeRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(resumeMapper::toDto)
                .toList();

        enrichResumesWithAdditionalData(resumes);
        validateResumesList(resumes, "Резюме для категории с ID: " + categoryId + " не найдены");
        return resumes;
    }

    @Override
    public ResumeFormDto convertToFormDto(ResumeDto dto) {
        return resumeMapper.toFormDto(dto);
    }

    private <T> void processResumeItems(Collection<T> items, ResumeDto resumeDto,
                                        BiConsumer<T, ResumeDto> setResumeFunc,
                                        Function<T, ?> createFunction) {
        if (items != null && !items.isEmpty()) {
            for (T item : items) {
                setResumeFunc.accept(item, resumeDto);
                createFunction.apply(item);
            }
        }
    }

    public boolean isResumeOwnedByApplicant(Long resumeId, Long applicantId) {
        return resumeRepository.existsByIdAndApplicantId(resumeId, applicantId);
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
        resume.setApplicant(userService.getUserById(resume.getApplicant().getId()));
    }

    private void validateResumesList(List<ResumeDto> resumes, String errorMessage) {
        if (resumes.isEmpty()) {
            log.warn(errorMessage);
            throw new ResumeNotFoundException(errorMessage);
        }
        log.info("Получено {} резюме", resumes.size());
    }
}
