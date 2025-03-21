package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.VacancyDao;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.exceptions.*;
import kg.attractor.job_search.exceptions.id.IncorrectCategoryIdException;
import kg.attractor.job_search.exceptions.id.IncorrectUserIdException;
import kg.attractor.job_search.exceptions.id.IncorrectVacancyIdException;
import kg.attractor.job_search.exceptions.notFound.EmployerNotFoundException;
import kg.attractor.job_search.mapper.VacancyMapper;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyDao vacancyDao;
    private final CategoryService categoryService;
    private final UserService userService;

    @Override
    public Long createVacancy(VacancyDto vacancyDto) {
        if(vacancyDto.getAuthorId() == null){
            throw new EmployerNotFoundException();
        }

        if(userService.getEmployerById(vacancyDto.getAuthorId()).isEmpty()){
            throw new EmployerNotFoundException("Не существует такого работодателя!");
        }

        if(categoryService.getCategoryIdIfPresent(vacancyDto.getCategoryId()).isEmpty()){
            throw new EmployerNotFoundException("Не существует такой категории!");
        }

        return vacancyDao.createVacancy(VacancyMapper.toVacancy(vacancyDto));
    }

    @Override
    public List<VacancyDto> getVacancies(){
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
        Optional<VacancyDto> vacancy = getVacancyById(vacancyId);
        if(vacancy.isEmpty()) {
            throw new IncorrectVacancyIdException("Не существует вакансии с таким id!");
        }

        if(!vacancyDto.getId().equals(vacancyId)){
            throw new IncorrectVacancyIdException("Неправильный id вакансии в теле запроса!");
        }

        vacancy.ifPresent(v ->{
            if(!v.getAuthorId().equals(vacancyDto.getAuthorId())){
                throw new IncorrectUserIdException("Вы не можете изменить автора вакансии!");
            }

            if(!v.getCreatedDate().equals(vacancyDto.getCreatedDate())){
                throw new IncorrectDateException("Вы не можете изменить дату создания вакансии!");
            }

            if(v.getCreatedDate().toInstant().isAfter(vacancyDto.getUpdateTime().toInstant())){
                throw new IncorrectDateException("Время обновления вакансии не может быть раньше времени создания!");
            }

            if(v.getUpdateTime().toInstant().isAfter(vacancyDto.getUpdateTime().toInstant())){
                throw new IncorrectDateException("Новое время обновления вакансии не может быть раньше прошлого!");
            }
        });

        Optional<Long> categoryId = categoryService.getCategoryIdIfPresent((vacancyDto.getCategoryId()));
        if(categoryId.isEmpty()){
            throw new IncorrectCategoryIdException("Не существует категории с таким id!");
        }

        return vacancyDao.updateVacancy(VacancyMapper.toVacancy(vacancyDto));
    }

    @Override
    public HttpStatus deleteVacancy(Long vacancyId) {
        if(vacancyDao.getVacancyById(vacancyId).isPresent()){
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
    public Optional<VacancyDto> getVacancyById(Long vacancyId){
        return vacancyDao.getVacancyById(vacancyId)
                .map(VacancyMapper::toVacancyDto);
    }

    @Override
    public List<VacancyDto> getVacanciesAppliedByUserId(Long applicantId){
        return vacancyDao.getVacanciesAppliedByUserId(applicantId)
                .stream()
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }

    @Override
    public List<VacancyDto> getVacanciesByEmployerId(Long employerId){
        return vacancyDao.getVacanciesByEmployerId(employerId)
                .stream()
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }

    @Override
    public List<VacancyDto> getVacanciesByCategoryName(String categoryName){
        String name = categoryName.trim().toLowerCase();
        return vacancyDao.getVacanciesByCategoryName(name)
                .stream()
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }
}
