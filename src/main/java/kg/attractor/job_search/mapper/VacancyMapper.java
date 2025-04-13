package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.vacancy.VacancyFormDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import kg.attractor.job_search.model.Vacancy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VacancyMapper {

    VacancyDto toDto(Vacancy vacancy);

    Vacancy toEntity(VacancyDto vacancyDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    Vacancy toEntity(VacancyFormDto vacancyFormDto);
}
