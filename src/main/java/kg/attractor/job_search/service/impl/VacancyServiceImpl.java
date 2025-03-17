package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.exceptions.ApplicantNotFoundException;
import kg.attractor.job_search.exceptions.EmployerNotFoundException;
import kg.attractor.job_search.mapper.VacancyMapper;
import kg.attractor.job_search.models.Vacancy;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final UserService userService;
    private final List<Vacancy> vacancies = new ArrayList<>();


    @SneakyThrows
    @Override
    public Long createVacancy(VacancyDto vacancyDto, Long employerId) {
        // TODO метод для создания вакансии (только для работодателей)
        // доработать с остальными типами dto
        UserDto employer = userService.getEmployerById(employerId);
        if (employer == null) {
            throw new EmployerNotFoundException();
        }
        vacancyDto.setAuthorId(employerId);
        vacancyDto.setCreatedDate(LocalDateTime.now());
        vacancyDto.setUpdateTime(LocalDateTime.now());
        vacancies.add(VacancyMapper.toVacancy(vacancyDto));
        return vacancyDto.getId();
    }

    @Override
    public List<VacancyDto> getActiveVacancies(Long applicantId) {
        UserDto applicant = userService.getApplicantById(applicantId);
        if (applicant == null) {
            throw new ApplicantNotFoundException();
        }

        //TODO получение всех активных вакансий
        return vacancies.stream()
                .filter(Vacancy::getIsActive)
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }

    @SneakyThrows
    @Override
    public Long updateVacancy(Long vacancyId, VacancyDto vacancyDto, Long employerId) {
        // TODO обновление вакансии для работодателей
        // доработать чтобы только именно создавший вакансию мог ее отредактировать
        int vacancyIndex = IntStream.range(0, vacancies.size())
                .filter(index -> vacancies.get(index).getId().equals(vacancyId))
                .findFirst()
                .orElse(-1);

        if (!vacancyDto.getId().equals(vacancyId)) {
            return null;
        }

        UserDto employer = userService.getEmployerById(employerId);
        if (employer == null) {
            throw new EmployerNotFoundException();
        }

        vacancies.set(vacancyIndex, VacancyMapper.toVacancy(vacancyDto));
        return vacancyDto.getId();
    }

    @SneakyThrows
    @Override
    public HttpStatus deleteVacancy(Long vacancyId, Long employerId) {
        //TODO  Удаление вакансии только для работодателя
        // доработать
        Vacancy vacancy = vacancies.stream()
                .filter(vacancy1 -> vacancy1.getId().equals(vacancyId))
                .findFirst()
                .orElse(null);

        if(vacancy == null) {
            return HttpStatus.NOT_FOUND;
        }

        if(!vacancy.getAuthorId().equals(employerId)) {
            return HttpStatus.BAD_REQUEST;
        }
        vacancies.remove(vacancy);
        return HttpStatus.OK;
    }

    @SneakyThrows
    @Override
    public List<VacancyDto> getVacanciesByCategoryId(Long categoryId, Long applicantId) {

        UserDto applicant = userService.getApplicantById(applicantId);
        if (applicant == null) {
            throw new ApplicantNotFoundException();
        }
        // TODO получение вакансий по категории
        return vacancies.stream()
                .filter(vacancy -> vacancy.getCategoryId().equals(categoryId) && vacancy.getIsActive())
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }
}
