package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.exception.nsee.ApplicantNotFoundException;
import kg.attractor.job_search.exception.nsee.EmployerNotFoundException;
import kg.attractor.job_search.exception.nsee.ResumeNotFoundException;
import kg.attractor.job_search.exception.nsee.VacancyNotFoundException;
import kg.attractor.job_search.mapper.ResumeMapper;
import kg.attractor.job_search.entity.Resume;
import kg.attractor.job_search.repository.ResumeRepository;
import kg.attractor.job_search.service.interfaces.CategoryService;
import kg.attractor.job_search.service.interfaces.ResumeService;
import kg.attractor.job_search.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
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

    @Transactional
    @Override
    public void updateResume(Long resumeId, Long userId) {
        Resume existing = getResumeById(resumeId);
        existing.setUpdatedAt(Timestamp.from(Instant.now()));

        if (!isResumeOwnedByApplicant(resumeId, userId))
            throw new AccessDeniedException("У вас нет прав на редактирование этого резюме");

        resumeRepository.save(existing);
        log.info("Обновлена дата обновления резюме: {}", resumeId);
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
    public List<ResumeDto> getLastResumes(Integer limit) {
        return findAndMapResumes(() -> resumeRepository.findLastResumes(limit), "Новые резюме не были найдены!");
    }

    @Override
    public Page<ResumeDto> getActiveResumesPage(String query, int page, int size, Long categoryId, String sortBy, String sortDirection) {
        Pageable pageable = createPageableWithSort(page, size, sortBy, sortDirection);
        if (categoryId != null) {
            return getResumesPageByCategoryId(query, categoryId, pageable);
        }
        return getResumesPage(query, pageable);
    }

    public Page<ResumeDto> getResumesPage(String query, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return getResumeDtoPage(() -> resumeRepository.findAllByIsActiveTrue(pageable), "Страница с резюме не найдена!");
        }
        return getResumeDtoPage(() -> resumeRepository.findAllActiveWithQuery(query, pageable),
                "Страница с резюме не найдена!");
    }

    public Page<ResumeDto> getResumesPageByCategoryId(String query, Long categoryId, Pageable pageable) {
        if (query != null && !query.isBlank()) {
            return getResumeDtoPage(() -> resumeRepository.findAllActiveByCategoryAndQuery(query, categoryId, pageable),
                    "Страница с резюме не найдена!");
        }
        return getResumeDtoPage(() -> resumeRepository.findAllByCategoryIdAndIsActiveTrue(categoryId, pageable),
                "Страница с резюме пользователя не была найдена!");
    }


    @Override
    public Page<ResumeDto> getResumesByApplicantId(Long applicantId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return getResumeDtoPage(() -> resumeRepository.findAllByApplicantId(applicantId, pageable),
                "Страница с активными резюме не найдена!");
    }


    private Pageable createPageableWithSort(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return switch (sortBy) {
            case "salary" -> PageRequest.of(page - 1, size, direction, "salary");
            case "createdAt" -> PageRequest.of(page - 1, size, direction, "createdAt");
            default -> PageRequest.of(page - 1, size, direction, "updatedAt");
        };
    }

    private Page<ResumeDto> getResumeDtoPage(Supplier<Page<Resume>> supplier, String notFoundMessage) {
        Page<Resume> resumePage = supplier.get();
        if (resumePage.isEmpty()) {
            throw new VacancyNotFoundException(notFoundMessage);
        }
        log.info("Получено {} резюме на странице", resumePage.getSize());
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
