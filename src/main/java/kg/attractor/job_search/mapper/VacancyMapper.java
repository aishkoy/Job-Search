package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.entity.Vacancy;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {RespondedApplicantMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VacancyMapper {

    VacancyDto toDto(Vacancy vacancy);

    Vacancy toEntity(VacancyDto vacancyDto);
}
