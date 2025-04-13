package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.VacancyDao;
import kg.attractor.job_search.dto.vacancy.VacancyFormDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import kg.attractor.job_search.exception.*;
import kg.attractor.job_search.mapper.VacancyMapper;
import kg.attractor.job_search.model.Vacancy;
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
    private final VacancyDao vacancyDao;
    private final VacancyMapper vacancyMapper;
    private final CategoryService categoryService;
    private final UserService userService;

    @Override
    public Long createVacancy(VacancyFormDto vacancyDto) {
        userService.getEmployerById(vacancyDto.getAuthorId());
        categoryService.getCategoryIdIfPresent(vacancyDto.getCategoryId());
        Long vacancyId = vacancyDao.createVacancy(vacancyMapper.toEntity(vacancyDto));

        log.info("Создана вакансия {}", vacancyId);

        return vacancyId;
    }

    @Override
    public List<VacancyDto> getVacancies() {
        List<VacancyDto> vacancies = vacancyDao.getVacancies()
                .stream()
                .map(this::mapAndEnrich)
                .toList();

        validateVacanciesList(vacancies, "Список вакансий пуст!");
        return vacancies;
    }

    @Override
    public List<VacancyDto> getActiveVacancies() {
        List<VacancyDto> activeVacancies = vacancyDao.getActiveVacancies()
                .stream()
                .map(this::mapAndEnrich)
                .toList();

        validateVacanciesList(activeVacancies, "Активных вакансий не найдено!");
        return activeVacancies;
    }

    @Override
    public Long updateVacancy(Long vacancyId, VacancyFormDto vacancyDto) {
        getVacancyById(vacancyId);
        if(!isVacancyOwnedByAuthor(vacancyId, vacancyDto.getAuthorId())) {
            throw new AccessDeniedException("У вас нет права на редактирование этой вакансии!");
        }

        categoryService.getCategoryIdIfPresent((vacancyDto.getCategoryId()));
        Vacancy vacancy = vacancyMapper.toEntity(vacancyDto);
        vacancy.setId(vacancyId);

        log.info("Обновлена вакансия {}", vacancyId);
        return vacancyDao.updateVacancy(vacancy);
    }

    @Override
    public HttpStatus deleteVacancy(Long vacancyId, Long authorId) {
        getVacancyById(vacancyId);
        if(!isVacancyOwnedByAuthor(vacancyId, authorId)) {
            throw new AccessDeniedException("У вас нет права на удалении этой вакансии!");
        }
        vacancyDao.deleteVacancy(vacancyId);
        log.info("Удалена вакансия {}", vacancyId);
        return HttpStatus.OK;
    }

    @Override
    public Long changeActiveStatus(Long vacancyId, Long authorId) {
        VacancyDto vacancy = getVacancyById(vacancyId);
        if(!isVacancyOwnedByAuthor(vacancyId, authorId)) {
            throw new AccessDeniedException("У вас нет права на изменение активности этой вакансии!");
        }
        Boolean status = vacancy.getIsActive() == Boolean.TRUE ? Boolean.FALSE : Boolean.TRUE;
        vacancyDao.updateVacancyActiveStatus(vacancyId, status);
        log.info("Изменен статус вакансии на {}", status);
        return vacancyId;
    }

    @Override
    public List<VacancyDto> getVacanciesByCategoryId(Long categoryId) {
        List<VacancyDto> vacancies = vacancyDao.getVacanciesByCategoryId(categoryId)
                .stream()
                .map(this::mapAndEnrich)
                .toList();
        validateVacanciesList(vacancies, "Вакансии по указанной категории не найдены!");
        return vacancies;
    }

    @Override
    public VacancyDto getVacancyById(Long vacancyId) {
        Vacancy vacancy = vacancyDao.getVacancyById(vacancyId).orElseThrow(() -> new VacancyNotFoundException("Не существует вакансии с таким id!"));
        return mapAndEnrich(vacancy);
    }

    @Override
    public List<VacancyDto> getVacanciesAppliedByUserId(Long applicantId) {
        List<VacancyDto> vacancies = vacancyDao.getVacanciesAppliedByUserId(applicantId)
                .stream()
                .map(this::mapAndEnrich)
                .toList();
        validateVacanciesList(vacancies, "Пользователь не откликался на вакансии!");
        return vacancies;
    }

    @Override
    public List<VacancyDto> getVacanciesByEmployerId(Long employerId) {
        List<VacancyDto> vacancies = vacancyDao.getVacanciesByEmployerId(employerId)
                .stream()
                .map(this::mapAndEnrich)
                .toList();
        validateVacanciesList(vacancies, "У работодателя нет опубликованных вакансий!");
        return vacancies;
    }

    @Override
    public List<VacancyDto> getVacanciesByCategoryName(String categoryName) {
        String name = categoryName.trim().toLowerCase();
        List<VacancyDto> vacancies =  vacancyDao.getVacanciesByCategoryName(name)
                .stream()
                .map(this::mapAndEnrich)
                .toList();
        validateVacanciesList(vacancies, "Вакансии по категории '" + categoryName + "' не найдены!");
        return vacancies;
    }

    public boolean isVacancyOwnedByAuthor(Long vacancyId, Long authorId) {
        return vacancyDao.isVacancyOwnedByAuthor(vacancyId, authorId);
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
        dto.setAuthorName(userService.getUserName(dto.getAuthorId()));
        return dto;
    }

}
