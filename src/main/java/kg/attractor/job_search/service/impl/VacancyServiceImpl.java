package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.vacancy.VacancyFormDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import kg.attractor.job_search.exception.*;
import kg.attractor.job_search.mapper.VacancyMapper;
import kg.attractor.job_search.entity.Vacancy;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyMapper vacancyMapper;
    private final CategoryService categoryService;
    private final UserService userService;
    private final VacancyRepository vacancyRepository;

    @Override
    public Long createVacancy(VacancyFormDto vacancyDto) {
        userService.getEmployerById(vacancyDto.getEmployer().getId());
        categoryService.getCategoryIfPresent(vacancyDto.getCategory().getId());

        Vacancy vacancy = vacancyMapper.toEntity(vacancyDto);
        vacancyRepository.save(vacancy);
        log.info("Создана вакансия {}", vacancy.getId());
        return vacancy.getId();
    }

    @Override
    public List<VacancyDto> getVacancies() {
        List<VacancyDto> vacancies = vacancyRepository.findAll()
                .stream()
                .map(this::mapAndEnrich)
                .toList();

        validateVacanciesList(vacancies, "Список вакансий пуст!");
        return vacancies;
    }

    @Override
    public List<VacancyDto> getActiveVacancies() {
        List<VacancyDto> activeVacancies = vacancyRepository.findAllByIsActiveTrue()
                .stream()
                .map(this::mapAndEnrich)
                .toList();

        validateVacanciesList(activeVacancies, "Активных вакансий не найдено!");
        return activeVacancies;
    }

    @Override
    public Long updateVacancy(Long vacancyId, VacancyFormDto vacancyDto) {
        VacancyDto existing = getVacancyDtoById(vacancyId);

        categoryService.getCategoryIfPresent((vacancyDto.getCategory().getId()));
        if (isVacancyNotOwnedByAuthor(vacancyId, vacancyDto.getEmployer().getId())) {
            throw new AccessDeniedException("У вас нет права на редактирование этой вакансии!");
        }

        Vacancy vacancy = vacancyMapper.toEntity(vacancyDto);
        vacancy.setId(existing.getId());
        vacancy.setCreatedAt(existing.getCreatedAt());
        vacancy.setEmployer(userService.getEntityById(existing.getEmployer().getId()));

        vacancyRepository.save(vacancy);
        log.info("Обновлена вакансия {}", vacancyId);
        return vacancyId;
    }

    @Override
    public HttpStatus deleteVacancy(Long vacancyId, Long authorId) {
        getVacancyDtoById(vacancyId);
        if (isVacancyNotOwnedByAuthor(vacancyId, authorId)) {
            throw new AccessDeniedException("У вас нет права на удалении этой вакансии!");
        }

        vacancyRepository.deleteById(vacancyId);
        log.info("Удалена вакансия {}", vacancyId);
        return HttpStatus.OK;
    }

    @Override
    public List<VacancyDto> getVacanciesByCategoryId(Long categoryId) {
        List<VacancyDto> vacancies = vacancyRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(this::mapAndEnrich)
                .toList();
        validateVacanciesList(vacancies, "Вакансии по указанной категории не найдены!");
        return vacancies;
    }

    @Override
    public VacancyDto getVacancyDtoById(Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new VacancyNotFoundException("Не существует вакансии с таким id!"));
        return mapAndEnrich(vacancy);
    }

    @Override
    public Vacancy getVacancyById(Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new VacancyNotFoundException("Не существует вакансии с таким id!"));
        log.info("Получена вакансия {} ", vacancyId);
        return vacancy;
    }

    @Override
    public VacancyDto getVacancyDtoByIdAndAuthor(Long vacancyId, Long authorId) {
        Vacancy vacancy = vacancyRepository.findByIdAndEmployerId(vacancyId, authorId)
                .orElseThrow(() -> new VacancyNotFoundException("Это не ваша вакансия"));
        return mapAndEnrich(vacancy);
    }

    @Override
    public List<VacancyDto> getVacanciesAppliedByUserId(Long applicantId) {
        List<VacancyDto> vacancies = vacancyRepository.findVacanciesAppliedByUserId(applicantId)
                .stream()
                .map(this::mapAndEnrich)
                .toList();
        validateVacanciesList(vacancies, "Пользователь не откликался на вакансии!");
        return vacancies;
    }

    @Override
    public List<VacancyDto> getVacanciesByEmployerId(Long employerId) {
        List<VacancyDto> vacancies = vacancyRepository.findAllByEmployerId(employerId)
                .stream()
                .map(this::mapAndEnrich)
                .toList();
        validateVacanciesList(vacancies, "У работодателя нет опубликованных вакансий!");
        return vacancies;
    }

    @Override
    public List<VacancyDto> getVacanciesByCategoryName(String categoryName) {
        String name = categoryName.trim().toLowerCase();
        List<VacancyDto> vacancies = vacancyRepository.findAllByCategoryName(name)
                .stream()
                .map(this::mapAndEnrich)
                .toList();
        validateVacanciesList(vacancies, "Вакансии по категории '" + categoryName + "' не найдены!");
        return vacancies;
    }

    @Override
    public List<VacancyDto> getLast3Vacancies() {
        List<VacancyDto> vacancies = vacancyRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .limit(3)
                .map(this::mapAndEnrich)
                .toList();
        validateVacanciesList(vacancies, "Новые вакансии не были найдены!");
        return vacancies;
    }

    @Override
    public VacancyFormDto convertToFormDto(VacancyDto dto) {
        return vacancyMapper.toFormDto(dto);
    }

    public boolean isVacancyNotOwnedByAuthor(Long vacancyId, Long authorId) {
        return !vacancyRepository.existsByIdAndEmployerId(vacancyId, authorId);
    }

    private void validateVacanciesList(List<VacancyDto> vacancies, String errorMessage) {
        if (vacancies.isEmpty()) {
            log.warn(errorMessage);
            throw new ResumeNotFoundException(errorMessage);
        }
        log.info("Получено {} вакансий", vacancies.size());
    }

    private VacancyDto mapAndEnrich(Vacancy vacancy) {
        VacancyDto dto = vacancyMapper.toDto(vacancy);
        dto.setEmployer(userService.getUserById(dto.getEmployer().getId()));
        return dto;
    }
}
