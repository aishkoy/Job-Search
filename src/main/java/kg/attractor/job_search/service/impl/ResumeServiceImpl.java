package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ResumeDao;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.resume.CreateResumeDto;
import kg.attractor.job_search.dto.resume.EditResumeDto;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.exception.*;
import kg.attractor.job_search.exception.ApplicantNotFoundException;
import kg.attractor.job_search.mapper.ResumeMapper;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeDao resumeDao;
    private final UserService userService;
    private final CategoryService categoryService;
    private final EducationInfoService educationInfoService;
    private final WorkExperienceInfoService workExperienceInfoService;
    private final ContactInfoService contactInfoService;

    @Override
    public List<ResumeDto> getResumes() {
        List<ResumeDto> resumes = resumeDao.getResumes().stream().map(ResumeMapper::toResumeDto).toList();
        enrichResumesWithAdditionalData(resumes);
        validateResumesList(resumes, "Резюме не найдены");
        log.info("Получено резюме: {}", resumes.size());
        return resumes;
    }

    @Override
    public List<ResumeDto> getActiveResumes() {
        List<ResumeDto> resumes = resumeDao.getActiveResumes()
                .stream()
                .map(ResumeMapper::toResumeDto)
                .toList();
        enrichResumesWithAdditionalData(resumes);
        validateResumesList(resumes, "Активные резюме не найдены");
        log.info("Получено активных резюме: {}", resumes.size());
        return resumes;
    }

    @Override
    public List<ResumeDto> getResumesByApplicantId(Long userId) {
        userService.getApplicantById(userId);
        List<ResumeDto> resumes = resumeDao.getResumesByApplicantId(userId)
                .stream()
                .map(ResumeMapper::toResumeDto)
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
                .map(ResumeMapper::toResumeDto)
                .toList();
        enrichResumesWithAdditionalData(resumes);
        validateResumesList(resumes, "Резюме для пользователя с именем: " + userName + " не найдены");
        return resumes;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Long createResume(CreateResumeDto resumeDto) {
        if (resumeDto.getApplicantId() == null) {
            throw new ApplicantNotFoundException();
        }

        userService.getApplicantById(resumeDto.getApplicantId());
        categoryService.getCategoryIdIfPresent(resumeDto.getCategoryId());

        Long resumeId = resumeDao.createResume(ResumeMapper.toResume(resumeDto));

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

        log.info("Created resume: {}", resumeDto);
        return resumeId;
    }

    @Override
    public ResumeDto getResumeById(Long resumeId) {
        Resume resume = resumeDao.getResumeById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Не существует резюме с таким id!"));

        ResumeDto resumeDto = ResumeMapper.toResumeDto(resume);
        enrichResumeWithAdditionalData(resumeDto);
        log.info("Retrieved resume: {}", resumeDto.getId());
        return resumeDto;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Long updateResume(Long resumeId, EditResumeDto resumeDto) {
        getResumeById(resumeId);
        categoryService.getCategoryIdIfPresent(resumeDto.getCategoryId());

        Resume resume = ResumeMapper.toResume(resumeDto);
        resume.setId(resumeId);
        Long updatedResumeId = resumeDao.updateResume(resume);

        updateResumeItems(
                resumeId,
                resumeDto.getEducations(),
                EducationInfoDto::setResumeId,
                educationInfoService::updateEducationInfo,
                educationInfoService::createEducationInfo
        );

        updateResumeItems(
                resumeId,
                resumeDto.getWorkExperiences(),
                WorkExperienceInfoDto::setResumeId,
                workExperienceInfoService::updateWorkExperienceInfo,
                workExperienceInfoService::createWorkExperience
        );

        updateResumeItems(
                resumeId,
                resumeDto.getContacts(),
                ContactInfoDto::setResumeId,
                contactInfoService::updateContactInfo,
                contactInfoService::createContactInfo
        );

        log.info("Updated resume: {}", resumeId);
        return updatedResumeId;
    }

    @Override
    public HttpStatus deleteResume(Long resumeId) {
        getResumeById(resumeId);
        resumeDao.deleteResume(resumeId);
        log.info("Deleted resume: {}", resumeId);
        return HttpStatus.OK;
    }

    @Override
    public List<ResumeDto> getResumesByCategoryId(Long categoryId) {
        Long category = categoryService.getCategoryIdIfPresent(categoryId);

        List<ResumeDto> resumes = resumeDao.getResumesByCategoryId(category)
                .stream()
                .map(ResumeMapper::toResumeDto)
                .toList();
        enrichResumesWithAdditionalData(resumes);
        validateResumesList(resumes, "Резюме для категории с ID: " + categoryId + " не найдены");
        log.info("Получено резюме по категории: {}", resumes.size());
        return resumes;
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

    private <T> void updateResumeItems(
            Long resumeId,
            List<T> newItems,
            ObjLongConsumer<T> setResumeIdFunc,
            BiFunction<Long, T, Long> updateFunction,
            Function<T, Long> createFunction
    ) {
        if (newItems != null) {
            for (T item : newItems) {
                try {
                    setResumeIdFunc.accept(item, resumeId);

                    Method getIdMethod = item.getClass().getMethod("getId");
                    Long itemId = (Long) getIdMethod.invoke(item);

                    if (itemId != null) {
                        updateFunction.apply(itemId, item);
                    } else {
                        createFunction.apply(item);
                    }
                } catch (NoSuchMethodException e) {
                    log.error("Метод getId() не найден для класса {}", item.getClass().getName());
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
    }

    private void validateResumesList(List<ResumeDto> resumes, String errorMessage) {
        if (resumes.isEmpty()) {
            log.warn(errorMessage);
            throw new ResumeNotFoundException(errorMessage);
        }
        log.info("Retrieved {} users", resumes.size());
    }
}
