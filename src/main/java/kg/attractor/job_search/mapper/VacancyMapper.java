package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.vacancy.EditVacancyDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import kg.attractor.job_search.model.Vacancy;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VacancyMapper {

    public Vacancy toVacancy(EditVacancyDto dto){
        return Vacancy.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .categoryId(dto.getCategoryId())
                .salary(dto.getSalary())
                .expFrom(dto.getExpFrom())
                .expTo(dto.getExpTo())
                .isActive(dto.getIsActive())
                .build();
    }
    public VacancyDto toVacancyDto(Vacancy vacancy) {
        return VacancyDto.builder()
                .id(vacancy.getId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .categoryId(vacancy.getCategoryId())
                .salary(vacancy.getSalary())
                .expFrom(vacancy.getExpFrom())
                .expTo(vacancy.getExpTo())
                .isActive(vacancy.getIsActive())
                .authorId(vacancy.getAuthorId())
                .createdDate(vacancy.getCreatedDate())
                .updateTime(vacancy.getUpdateTime())
                .build();
    }

    public Vacancy toVacancy(VacancyDto dto) {
        return Vacancy.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .categoryId(dto.getCategoryId())
                .salary(dto.getSalary())
                .expFrom(dto.getExpFrom())
                .expTo(dto.getExpTo())
                .isActive(dto.getIsActive())
                .authorId(dto.getAuthorId())
                .createdDate(dto.getCreatedDate())
                .updateTime(dto.getUpdateTime())
                .build();
    }
}
