package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.exception.*;
import kg.attractor.job_search.exception.ApplicantNotFoundException;
import kg.attractor.job_search.mapper.ResumeMapper;
import kg.attractor.job_search.entity.Resume;
import kg.attractor.job_search.repository.ResumeRepository;
import kg.attractor.job_search.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Service("resumeService")
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public Long createResume(ResumeDto resumeDto) {
        if (resumeDto.getApplicant() == null) throw new ApplicantNotFoundException();
        categoryService.getCategoryIfPresent(resumeDto.getCategory().getId());

        Resume resume = resumeMapper.toEntity(resumeDto);
        linkResumeChildren(resume);
        resume = resumeRepository.save(resume);

        log.info("Созданное резюме: {}", resume.getId());
        return resume.getId();
    }

    @Override
    @Transactional
    public Long updateResume(Long resumeId, ResumeDto form) {
        ResumeDto existing = getResumeDtoById(resumeId);
        form.setCreatedAt(existing.getCreatedAt());
        categoryService.getCategoryIfPresent(form.getCategory().getId());

        if (!isResumeOwnedByApplicant(resumeId, form.getApplicant().getId()))
            throw new AccessDeniedException("У вас нет прав на редактирование этого резюме");

        Resume resume = resumeMapper.toEntity(form);

        resumeRepository.save(resume);
        log.info("Обновлено резюме: {}", resumeId);
        return resumeId;
    }

    @Override
    public HttpStatus deleteResume(Long resumeId, Long userId) {
        getResumeDtoById(resumeId);
        if (!isResumeOwnedByApplicant(resumeId, userId))
            throw new AccessDeniedException("У вас нет прав на удаление этого резюме!");
        resumeRepository.deleteById(resumeId);
        log.info("Удалено резюме: {}", resumeId);
        return HttpStatus.OK;
    }

    @Override
    public List<ResumeDto> getResumes() {
        return findAndMapResumes(resumeRepository::findAll, "Резюме не найдены");
    }

    @Override
    public List<ResumeDto> getActiveResumes() {
        return findAndMapResumes(resumeRepository::findAllByIsActiveTrue, "Активные резюме не найдены");
    }

    @Override
    public List<ResumeDto> getResumesByApplicantId(Long userId) {
        userService.getApplicantById(userId);
        return findAndMapResumes(() -> resumeRepository.findAllByApplicantId(userId),
                "Резюме для пользователя с ID: " + userId + " не найдены");
    }

    @Override
    public List<ResumeDto> getResumesByApplicantName(String name) {
        String formattedName = StringUtils.capitalize(name.trim().toLowerCase());
        userService.getUsersByName(formattedName);
        return findAndMapResumes(() -> resumeRepository.findAllByApplicantName(formattedName),
                "Резюме для пользователя с именем: " + formattedName + " не найдены");
    }

    @Override
    public Resume getResumeById(Long resumeId, Long userId) {
        Resume resume = getResumeById(resumeId);
        if (isUserAuthorizedForResume(userId, resumeId)) return resume;
        throw new AccessDeniedException("Это не ваше резюме");
    }

    @Override
    public Resume getResumeById(Long resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Не существует резюме с таким id!"));
    }

    @Override
    public ResumeDto getResumeDtoById(Long resumeId, Long userId) {
        ResumeDto resume = getResumeDtoById(resumeId);
        if (isUserAuthorizedForResume(userId, resumeId)) return resume;
        throw new AccessDeniedException("Это не ваше резюме");
    }

    @Override
    public ResumeDto getResumeDtoById(Long resumeId) {
        return resumeMapper.toDto(getResumeById(resumeId));
    }

    @Override
    public List<ResumeDto> getResumesByCategoryId(Long categoryId) {
        categoryService.getCategoryIfPresent(categoryId);
        return findAndMapResumes(() -> resumeRepository.findAllByCategoryId(categoryId),
                "Резюме для категории с ID: " + categoryId + " не найдены");
    }

    @Override
    public List<ResumeDto> getLastResumes() {
        return findAndMapResumes(() -> resumeRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .limit(4)
                .toList(), "Новые резюме не были найдены!");
    }

    @Override
    public Page<ResumeDto> getActiveResumesPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return getResumeDtoPage(() -> resumeRepository.findAllByIsActiveTrue(pageable),
                "Страница с активными резюме не найдена!");
    }

    @Override
    public Page<ResumeDto> getResumesByApplicantId(Long applicantId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return getResumeDtoPage(() -> resumeRepository.findAllByApplicantId(applicantId, pageable),
                "Страница с активными резюме не найдена!");
    }


    @Override
    public Page<ResumeDto> getResumesPageByCategoryId(int page, int size, Long categoryId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return getResumeDtoPage(() -> resumeRepository.findAllByCategoryId(categoryId, pageable),
                "Страница с резюме пользователя не была найдена!");
    }


    @Override
    public void addExperience(ResumeDto form) {
        addItemToList(form.getWorkExperiences(), WorkExperienceInfoDto::new, form::setWorkExperiences);
    }

    @Override
    public void addEducation(ResumeDto form) {
        addItemToList(form.getEducations(), EducationInfoDto::new, form::setEducations);
    }

    @Override
    public void addContact(ResumeDto form) {
        addItemToList(form.getContacts(), ContactInfoDto::new, form::setContacts);
    }

    private Page<ResumeDto> getResumeDtoPage(Supplier<Page<Resume>> supplier, String notFoundMessage) {
        Page<Resume> resumePage = supplier.get();
        if (resumePage.isEmpty()) {
            throw new VacancyNotFoundException(notFoundMessage);
        }
        log.info("Получено {} вакансий на странице", resumePage.getSize());
        return resumePage.map(resumeMapper::toDto);
    }

    public boolean isAuthorOfResume(Long resumeId, Long userId) {
        Resume resume = getResumeById(resumeId);
        return resume.getApplicant().getId().equals(userId);
    }

    private List<ResumeDto> findAndMapResumes(Supplier<List<Resume>> supplier, String errorMessage) {
        List<ResumeDto> resumes = supplier.get()
                .stream()
                .map(resumeMapper::toDto)
                .toList();

        if (resumes.isEmpty()) {
            log.warn(errorMessage);
            throw new ResumeNotFoundException(errorMessage);
        }

        log.info("Получено {} резюме", resumes.size());
        return resumes;
    }

    private void linkResumeChildren(Resume resume) {
        resume.getContacts().forEach(c -> c.setResume(resume));
        resume.getEducations().forEach(e -> e.setResume(resume));
        resume.getWorkExperiences().forEach(w -> w.setResume(resume));
    }

    private <T> void addItemToList(List<T> list, Supplier<T> creator, Consumer<List<T>> setter) {
        if (list == null) list = new ArrayList<>();
        list.add(creator.get());
        setter.accept(list);
    }

    public boolean isResumeOwnedByApplicant(Long resumeId, Long applicantId) {
        return resumeRepository.existsByIdAndApplicantId(resumeId, applicantId);
    }

    private boolean isUserAuthorizedForResume(Long userId, Long resumeId) {
        try {
            userService.getEmployerById(userId);
            return true;
        } catch (EmployerNotFoundException e) {
            return isResumeOwnedByApplicant(resumeId, userId);
        }
    }
}
