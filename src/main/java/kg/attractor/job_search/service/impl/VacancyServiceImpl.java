package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.vacancy.VacancyDto;
import kg.attractor.job_search.dto.vacancy.VacancyFormDto;
import kg.attractor.job_search.entity.Vacancy;
import kg.attractor.job_search.exception.VacancyNotFoundException;
import kg.attractor.job_search.mapper.VacancyMapper;
import kg.attractor.job_search.repository.VacancyRepository;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyMapper vacancyMapper;
    private final VacancyRepository vacancyRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    @Override
    public Long createVacancy(VacancyFormDto dto) {
        userService.getEmployerById(dto.getEmployer().getId());
        categoryService.getCategoryIfPresent(dto.getCategory().getId());

        Vacancy vacancy = vacancyMapper.toEntity(dto);
        vacancyRepository.save(vacancy);
        log.info("Создана вакансия {}", vacancy.getId());
        return vacancy.getId();
    }

    @Override
    public Long updateVacancy(Long vacancyId, VacancyFormDto dto) {
        Vacancy existing = getVacancyById(vacancyId);
        validateOwnership(vacancyId, dto.getEmployer().getId());
        categoryService.getCategoryIfPresent(dto.getCategory().getId());

        Vacancy updated = vacancyMapper.toEntity(dto);
        updated.setId(existing.getId());
        updated.setCreatedAt(existing.getCreatedAt());
        updated.setIsActive(dto.getIsActive());

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
    public List<VacancyDto> getLast3Vacancies() {
        return findAndMapVacancies(() -> vacancyRepository.findAllByOrderByCreatedAtDesc().stream().limit(3).toList(),
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
    public VacancyFormDto convertToFormDto(VacancyDto dto) {
        return vacancyMapper.toFormDto(dto);
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
