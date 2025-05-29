package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.entity.Vacancy;
import kg.attractor.job_search.exception.nsee.VacancyNotFoundException;
import kg.attractor.job_search.mapper.VacancyMapper;
import kg.attractor.job_search.repository.VacancyRepository;
import kg.attractor.job_search.service.interfaces.CategoryService;
import kg.attractor.job_search.service.interfaces.UserService;
import kg.attractor.job_search.service.interfaces.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Service("vacancyService")
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyMapper vacancyMapper;
    private final VacancyRepository vacancyRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    @Override
    public Long createVacancy(VacancyDto dto) {
        userService.getEmployerById(dto.getEmployer().getId());
        categoryService.getCategoryIfPresent(dto.getCategory().getId());

        Vacancy vacancy = vacancyMapper.toEntity(dto);
        vacancyRepository.save(vacancy);
        log.info("Создана вакансия {}", vacancy.getId());
        return vacancy.getId();
    }

    @Transactional
    @Override
    public void updateVacancy(Long vacancyId, Long userId) {
        Vacancy existing = getVacancyById(vacancyId);
        validateOwnership(vacancyId, userId);
        existing.setUpdatedAt(Timestamp.from(Instant.now()));

        vacancyRepository.save(existing);
        log.info("Обновлено время вакансии {}", vacancyId);
    }

    @Transactional
    @Override
    public Long updateVacancy(Long vacancyId, VacancyDto dto) {
        Vacancy existing = getVacancyById(vacancyId);
        categoryService.getCategoryIfPresent(dto.getCategory().getId());
        validateOwnership(vacancyId, dto.getEmployer().getId());

        dto.setCreatedAt(existing.getCreatedAt());
        Vacancy updated = vacancyMapper.toEntity(dto);

        vacancyRepository.save(updated);
        log.info("Обновлена вакансия {}", vacancyId);
        return vacancyId;
    }

    @Override
    public HttpStatus deleteVacancy(Long vacancyId, Long authorId) {
        getVacancyDtoById(vacancyId);
        validateOwnership(vacancyId, authorId);

        vacancyRepository.deleteById(vacancyId);
        log.info("Удалена вакансия {}", vacancyId);
        return HttpStatus.OK;
    }

    @Override
    public List<VacancyDto> getLastVacancies(Integer limit) {
        return findAndMapVacancies(() -> vacancyRepository.findLastVacancies(limit)
        );
    }

    @Override
    public VacancyDto getVacancyDtoById(Long vacancyId) {
        Vacancy vacancy = getVacancyById(vacancyId);
        return mapAndEnrich(vacancy);
    }

    @Override
    public Vacancy getVacancyById(Long vacancyId) {
        return vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new VacancyNotFoundException("Не существует вакансии с таким id!"));
    }

    @Override
    public VacancyDto getVacancyDtoByIdAndAuthor(Long vacancyId, Long authorId) {
        Vacancy vacancy = vacancyRepository.findByIdAndEmployerId(vacancyId, authorId)
                .orElseThrow(() -> new VacancyNotFoundException("Это не ваша вакансия"));
        return mapAndEnrich(vacancy);
    }

    @Override
    public Page<VacancyDto> getVacanciesByEmployerId(Long employer, int page, int size) {
        Pageable pageable = createPageableWithSort(page, size, "updatedAt", "asc");
        return getVacancyDtoPage(() -> vacancyRepository.findAllByEmployerId(employer, pageable), "У данного работодателя пока нет вакансий!");
    }

    @Override
    public Page<VacancyDto> getActiveVacanciesPage(String query, int page, int size, Long categoryId, String sortBy, String sortDirection) {
        Pageable pageable = createPageableWithSort(page, size, sortBy, sortDirection);

        return categoryId != null
                ? getVacanciesPageByCategoryId(query, categoryId, pageable)
                : getActiveVacanciesPage(query, pageable);
    }

    public Page<VacancyDto> getActiveVacanciesPage(String query, Pageable pageable) {
        if (query != null && !query.isBlank()) {
            return getVacancyDtoPage(
                    () -> vacancyRepository.findAllByIsActiveTrueWithQuery(query, pageable),
                    "Активных вакансий не найдено!"
            );
        }

        return getVacancyDtoPage(
                () -> vacancyRepository.findAllByIsActiveTrue(pageable),
                "Активных вакансий не найдено!"
        );
    }

    public Page<VacancyDto> getVacanciesPageByCategoryId(String query, Long categoryId, Pageable pageable) {
        List<Long> categories = categoryService.findCategoriesById(categoryId);

        if (query != null && !query.isBlank()) {
            return getVacancyDtoPage(
                    () -> vacancyRepository.findAllByCategoryIdsAndQuery(query, categories, pageable),
                    "Вакансии по указанной категории не найдены!"
            );
        }

        return getVacancyDtoPage(
                () -> vacancyRepository.findAllByCategoryIds(categories, pageable),
                "Вакансии по указанной категории не найдены!"
        );
    }

    private Pageable createPageableWithSort(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return switch (sortBy) {
            case "responses" -> PageRequest.of(page - 1, size, direction, "responses");
            case "salary" -> PageRequest.of(page - 1, size, direction, "salary");
            case "createdAt" -> PageRequest.of(page - 1, size, direction, "createdAt");
            default -> PageRequest.of(page - 1, size, direction, "updatedAt");
        };
    }

    private Page<VacancyDto> getVacancyDtoPage(Supplier<Page<Vacancy>> supplier, String notFoundMessage) {
        Page<Vacancy> vacancyPage = supplier.get();
        if (vacancyPage.isEmpty()) {
            throw new VacancyNotFoundException(notFoundMessage);
        }
        log.info("Получено {} вакансий на странице", vacancyPage.getSize());
        return vacancyPage.map(this::mapAndEnrich);
    }

    public boolean isAuthorOfVacancy(Long vacancyId, Long userId) {
        Vacancy vacancy = getVacancyById(vacancyId);
        return vacancy.getEmployer().getId().equals(userId);
    }

    private void validateOwnership(Long vacancyId, Long authorId) {
        if (!vacancyRepository.existsByIdAndEmployerId(vacancyId, authorId)) {
            throw new AccessDeniedException("У вас нет права на выполнение этого действия!");
        }
    }

    private List<VacancyDto> findAndMapVacancies(Supplier<List<Vacancy>> supplier) {
        List<VacancyDto> vacancies = supplier.get().stream()
                .map(this::mapAndEnrich)
                .toList();

        if (vacancies.isEmpty()) {
            log.warn("Новые вакансии не были найдены!");
            throw new VacancyNotFoundException("Новые вакансии не были найдены!");
        }

        log.info("Получено {} вакансий", vacancies.size());
        return vacancies;
    }

    private VacancyDto mapAndEnrich(Vacancy vacancy) {
        VacancyDto dto = vacancyMapper.toDto(vacancy);
        dto.setEmployer(userService.getUserById(dto.getEmployer().getId()));
        return dto;
    }
}
