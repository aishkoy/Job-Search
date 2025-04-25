package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.entity.Vacancy;
import kg.attractor.job_search.exception.VacancyNotFoundException;
import kg.attractor.job_search.mapper.VacancyMapper;
import kg.attractor.job_search.repository.VacancyRepository;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

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

    @Override
    public Long updateVacancy(Long vacancyId, VacancyDto dto) {
        Vacancy existing = getVacancyById(vacancyId);
        dto.setCreatedAt(existing.getCreatedAt());
        validateOwnership(vacancyId, dto.getEmployer().getId());
        categoryService.getCategoryIfPresent(dto.getCategory().getId());

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
    public List<VacancyDto> getVacancies() {
        return findAndMapVacancies(vacancyRepository::findAll, "Список вакансий пуст!");
    }

    @Override
    public List<VacancyDto> getActiveVacancies() {
        return findAndMapVacancies(vacancyRepository::findAllByIsActiveTrue, "Активных вакансий не найдено!");
    }

    @Override
    public List<VacancyDto> getVacanciesByCategoryId(Long categoryId) {
        return findAndMapVacancies(() -> vacancyRepository.findAllByCategoryId(categoryId),
                "Вакансии по указанной категории не найдены!");
    }

    @Override
    public List<VacancyDto> getVacanciesByCategoryName(String categoryName) {
        String name = categoryName.trim().toLowerCase();
        return findAndMapVacancies(() -> vacancyRepository.findAllByCategoryName(name),
                "Вакансии по категории '" + categoryName + "' не найдены!");
    }

    @Override
    public List<VacancyDto> getVacanciesByEmployerId(Long employerId) {
        return findAndMapVacancies(() -> vacancyRepository.findAllByEmployerId(employerId),
                "У работодателя нет опубликованных вакансий!");
    }

    @Override
    public List<VacancyDto> getVacanciesAppliedByUserId(Long applicantId) {
        return findAndMapVacancies(() -> vacancyRepository.findVacanciesAppliedByUserId(applicantId),
                "Пользователь не откликался на вакансии!");
    }

    @Override
    public List<VacancyDto> getLastVacancies() {
        return findAndMapVacancies(() -> vacancyRepository.findAllByOrderByCreatedAtDesc()
                        .stream()
                        .limit(4)
                        .toList(),
                "Новые вакансии не были найдены!");
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
    public Page<VacancyDto> getVacanciesPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return getVacancyDtoPage(
                () -> vacancyRepository.findAll(pageable),
                "Список вакансий пуст!");
    }

    @Override
    public Page<VacancyDto> getActiveVacanciesPage(int page, int size, Long categoryId, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = createPageableWithSort(page, size, sortBy, direction);

        return categoryId != null
                ? getVacanciesPageByCategoryId(page, size, categoryId, sortBy, sortDirection)
                : getVacancyDtoPage(
                () -> vacancyRepository.findAllByIsActiveTrue(pageable),
                "Активных вакансий не найдено!"
        );
    }

    private Pageable createPageableWithSort(int page, int size, String sortBy, Sort.Direction direction) {
        return switch (sortBy) {
            case "responses" -> PageRequest.of(page - 1, size, direction, "responses");
            default -> PageRequest.of(page - 1, size, direction, "createdAt");
        };
    }

    @Override
    public Page<VacancyDto> getVacanciesPageByCategoryId(int page, int size, Long categoryId, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = createPageableWithSort(page, size, sortBy, direction);

        return getVacancyDtoPage(
                () -> vacancyRepository.findAllByCategoryId(categoryId, pageable),
                "Вакансии по указанной категории не найдены!"
        );
    }

    @Override
    public Page<VacancyDto> getVacanciesPageByEmployer(int page, int size, Long employerId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return getVacancyDtoPage(
                () -> vacancyRepository.findAllByEmployerId(employerId, pageable),
                "У работодателя нет опубликованных вакансий!");
    }

    @Override
    public Page<VacancyDto> getVacanciesPageByCategoryId(int page, int size, Long categoryId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return getVacancyDtoPage(
                () -> vacancyRepository.findAllByCategoryId(categoryId, pageable),
                "Вакансии по указанной категории не найдены!");
    }

    @Override
    public Page<VacancyDto> getVacanciesPageByCategoryName(int page, int size, String categoryName) {
        String name = categoryName.trim().toLowerCase();
        Pageable pageable = PageRequest.of(page - 1, size);
        return getVacancyDtoPage(
                () -> vacancyRepository.findAllByCategoryName(name, pageable),
                "Вакансии по указанной категории не найдены!");
    }

    @Override
    public Page<VacancyDto> getVacanciesPageByAppliedUser(int page, int size, Long applicantId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return getVacancyDtoPage(
                () -> vacancyRepository.findVacanciesAppliedByUserId(applicantId, pageable),
                "Пользователь не откликался на вакансии!");
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

    private List<VacancyDto> findAndMapVacancies(Supplier<List<Vacancy>> supplier, String errorMessage) {
        List<VacancyDto> vacancies = supplier.get().stream()
                .map(this::mapAndEnrich)
                .toList();

        if (vacancies.isEmpty()) {
            log.warn(errorMessage);
            throw new VacancyNotFoundException(errorMessage);
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
