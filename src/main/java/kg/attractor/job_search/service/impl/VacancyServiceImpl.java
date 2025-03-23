package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.mapper.VacancyMapper;
import kg.attractor.job_search.models.Vacancy;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final List<Vacancy> vacancies = new ArrayList<>();


    @SneakyThrows
    @Override
    public Long createVacancy(VacancyDto vacancyDto) {
        // TODO метод для создания вакансии (только для работодателей)
        // доработать с остальными типами dto
        vacancies.add(VacancyMapper.toVacancy(vacancyDto));
        return vacancyDto.getId();
    }

    @Override
    public List<VacancyDto> getActiveVacancies() {
        //TODO получение всех активных вакансий
        return vacancies.stream()
                .filter(Vacancy::getIsActive)
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }

    @SneakyThrows
    @Override
    public Long updateVacancy(Long vacancyId, VacancyDto vacancyDto) {
        // TODO обновление вакансии для работодателей
        // доработать чтобы только именно создавший вакансию мог ее отредактировать
        int vacancyIndex = IntStream.range(0, vacancies.size())
                .filter(index -> vacancies.get(index).getId().equals(vacancyId))
                .findFirst()
                .orElse(-1);

        vacancies.set(vacancyIndex, VacancyMapper.toVacancy(vacancyDto));
        return vacancyDto.getId();
    }

    @SneakyThrows
    @Override
    public HttpStatus deleteVacancy(Long vacancyId) {
        //TODO  Удаление вакансии только для работодателя
        // доработать
        Vacancy vacancy = vacancies.stream()
                .filter(vacancy1 -> vacancy1.getId().equals(vacancyId))
                .findFirst()
                .orElse(null);

        if(vacancy == null) {
            return HttpStatus.NOT_FOUND;
        }

        vacancies.remove(vacancy);
        return HttpStatus.OK;
    }

    @SneakyThrows
    @Override
    public List<VacancyDto> getVacanciesByCategoryId(Long categoryId) {
        // TODO получение вакансий по категории
        return vacancies.stream()
                .filter(vacancy -> vacancy.getCategoryId().equals(categoryId) && vacancy.getIsActive())
                .map(VacancyMapper::toVacancyDto)
                .toList();
    }
}
