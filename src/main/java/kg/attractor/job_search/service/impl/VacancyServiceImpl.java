package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.VacancyDao;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import kg.attractor.job_search.exceptions.*;
import kg.attractor.job_search.exceptions.EmployerNotFoundException;
import kg.attractor.job_search.mapper.VacancyMapper;
import kg.attractor.job_search.models.Vacancy;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyDao vacancyDao;
    private final CategoryService categoryService;
    private final UserService userService;

    @Override
    public Long createVacancy(VacancyDto vacancyDto) {
        if (vacancyDto.getAuthorId() == null) {
            throw new EmployerNotFoundException();
        }

        userService.getEmployerById(vacancyDto.getAuthorId());
        categoryService.getCategoryIdIfPresent(vacancyDto.getCategoryId());
        return vacancyDao.createVacancy(VacancyMapper.toVacancy(vacancyDto));
    }

    @Override
    public List<VacancyDto> getVacancies() {
        return vacancyDao.getVacancies()
                .stream()
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }

    @Override
    public List<VacancyDto> getActiveVacancies() {
        return vacancyDao.getActiveVacancies()
                .stream()
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }

    @Override
    public Long updateVacancy(Long vacancyId, VacancyDto vacancyDto) {
        VacancyDto vacancy = getVacancyById(vacancyId);

        if (!vacancyDto.getId().equals(vacancyId)) {
            throw new VacancyNotFoundException("Неправильный id вакансии в теле запроса!");
        }

        if (!vacancy.getAuthorId().equals(vacancyDto.getAuthorId())) {
            throw new EmployerNotFoundException("Вы не можете изменить автора вакансии!");
        }

        if (!vacancy.getCreatedDate().equals(vacancyDto.getCreatedDate())) {
            throw new IncorrectDateException("Вы не можете изменить дату создания вакансии!");
        }

        if (vacancy.getCreatedDate().toInstant().isAfter(vacancyDto.getUpdateTime().toInstant())) {
            throw new IncorrectDateException("Время обновления вакансии не может быть раньше времени создания!");
        }

        if (vacancy.getUpdateTime().toInstant().isAfter(vacancyDto.getUpdateTime().toInstant())) {
            throw new IncorrectDateException("Новое время обновления вакансии не может быть раньше прошлого!");
        }

        categoryService.getCategoryIdIfPresent((vacancyDto.getCategoryId()));

        return vacancyDao.updateVacancy(VacancyMapper.toVacancy(vacancyDto));
    }

    @Override
    public HttpStatus deleteVacancy(Long vacancyId) {
        if (vacancyDao.getVacancyById(vacancyId).isPresent()) {
            vacancyDao.deleteVacancy(vacancyId);
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public List<VacancyDto> getVacanciesByCategoryId(Long categoryId) {
        return vacancyDao.getVacanciesByCategoryId(categoryId)
                .stream()
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }

    @Override
    public VacancyDto getVacancyById(Long vacancyId) {
        Vacancy vacancy = vacancyDao.getVacancyById(vacancyId).orElseThrow(() -> new VacancyNotFoundException("Не существует вакансии с таким id!"));
        return VacancyMapper.toVacancyDto(vacancy);
    }

    @Override
    public List<VacancyDto> getVacanciesAppliedByUserId(Long applicantId) {
        return vacancyDao.getVacanciesAppliedByUserId(applicantId)
                .stream()
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }

    @Override
    public List<VacancyDto> getVacanciesByEmployerId(Long employerId) {
        return vacancyDao.getVacanciesByEmployerId(employerId)
                .stream()
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }

    @Override
    public List<VacancyDto> getVacanciesByCategoryName(String categoryName) {
        String name = categoryName.trim().toLowerCase();
        return vacancyDao.getVacanciesByCategoryName(name)
                .stream()
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }
}
