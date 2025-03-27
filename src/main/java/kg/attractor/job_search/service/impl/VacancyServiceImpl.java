package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.VacancyDao;
import kg.attractor.job_search.dto.vacancy.EditVacancyDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import kg.attractor.job_search.exception.*;
import kg.attractor.job_search.exception.EmployerNotFoundException;
import kg.attractor.job_search.mapper.VacancyMapper;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        Long vacancyId = vacancyDao.createVacancy(VacancyMapper.toVacancy(vacancyDto));

        log.info("Vacancy created {}", vacancyId);

        return vacancyId;
    }

    @Override
    public List<VacancyDto> getVacancies() {
        List<VacancyDto> vacancies = vacancyDao.getVacancies()
                .stream()
                .map(VacancyMapper::toVacancyDto)
                .toList();
        log.info("Vacancies retrieved {}", vacancies.size());
        return vacancies;
    }

    @Override
    public List<VacancyDto> getActiveVacancies() {
        List<VacancyDto> activeVacancies = vacancyDao.getActiveVacancies()
                .stream()
                .map(VacancyMapper::toVacancyDto)
                .toList();

        log.info("Active vacancies {}", activeVacancies.size());
        return activeVacancies;
    }

    @Override
    public Long updateVacancy(Long vacancyId, EditVacancyDto vacancyDto) {
        getVacancyById(vacancyId);

        categoryService.getCategoryIdIfPresent((vacancyDto.getCategoryId()));
        Vacancy vacancy = VacancyMapper.toVacancy(vacancyDto);
        vacancy.setId(vacancyId);

        log.info("Updated vacancy {}", vacancyId);
        return vacancyDao.updateVacancy(vacancy);
    }

    @Override
    public HttpStatus deleteVacancy(Long vacancyId) {
        vacancyDao.getVacancyById(vacancyId);
        vacancyDao.deleteVacancy(vacancyId);
        log.info("Deleted vacancy {}", vacancyId);
        return HttpStatus.OK;
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
