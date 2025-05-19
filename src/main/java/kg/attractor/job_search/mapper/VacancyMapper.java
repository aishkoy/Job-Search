package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.entity.Vacancy;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {ResponseMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VacancyMapper {

    @Mapping(target = "responses", expression = "java(vacancy.getResponses() != null ? vacancy.getResponses().size() : 0)")
    VacancyDto toDto(Vacancy vacancy);

    @Mapping(target = "responses", ignore = true)
    Vacancy toEntity(VacancyDto vacancyDto);
}
