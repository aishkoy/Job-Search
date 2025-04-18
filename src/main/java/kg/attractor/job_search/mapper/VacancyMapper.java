package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.vacancy.VacancyFormDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import kg.attractor.job_search.entity.Vacancy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface VacancyMapper {

    VacancyDto toDto(Vacancy vacancy);

    Vacancy toEntity(VacancyDto vacancyDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", source = "vacancyFormDto", qualifiedByName = "mapIsActive")
    Vacancy toEntity(VacancyFormDto vacancyFormDto);

    VacancyFormDto toFormDto(VacancyDto vacancyDto);

    @Named("mapIsActive")
    default Boolean mapIsActive(VacancyFormDto dto) {
        return dto.getIsActive();
    }
}
