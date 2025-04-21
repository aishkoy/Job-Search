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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public List<ResumeDto> getResumes() {
        List<ResumeDto> resumes =
                resumeRepository.findAll()
                        .stream()
                        .map(resumeMapper::toDto)
                        .toList();
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

        Resume resume = resumeMapper.toEntity(resumeDto);

        final Resume finalResume = resume;

        if (resume.getContacts() != null) {
            resume.getContacts().forEach(contact -> contact.setResume(finalResume));
        }
        if (resume.getEducations() != null) {
            resume.getEducations().forEach(education -> education.setResume(finalResume));
        }
        if (resume.getWorkExperiences() != null) {
            resume.getWorkExperiences().forEach(work -> work.setResume(finalResume));
        }

        resume = resumeRepository.save(resume);

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

        return resumeMapper.toDto(resume);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Long updateResume(Long resumeId, ResumeFormDto resumeForm) {
        ResumeDto existing = getResumeDtoById(resumeId);
        categoryService.getCategoryIfPresent(resumeForm.getCategory().getId());

        if (!isResumeOwnedByApplicant(resumeId, resumeForm.getApplicant().getId())) {
            throw new AccessDeniedException("У вас нет прав на редактирование этого резюме");
        }

        Resume resume = resumeMapper.toEntity(resumeForm);
        resume.setId(resumeId);
        resume.setCreatedAt(existing.getCreatedAt());
        resume.setIsActive(resumeForm.getIsActive());
        resumeRepository.save(resume);

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

        validateResumesList(resumes, "Резюме для категории с ID: " + categoryId + " не найдены");
        return resumes;
    }

    @Override
    public ResumeFormDto convertToFormDto(ResumeDto dto) {
        return resumeMapper.toFormDto(dto);
    }

    public boolean isResumeOwnedByApplicant(Long resumeId, Long applicantId) {
        return resumeRepository.existsByIdAndApplicantId(resumeId, applicantId);
    }

    private void validateResumesList(List<ResumeDto> resumes, String errorMessage) {
        if (resumes.isEmpty()) {
            log.warn(errorMessage);
            throw new ResumeNotFoundException(errorMessage);
        }
        log.info("Получено {} резюме", resumes.size());
    }
}
