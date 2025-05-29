package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {VacancyMapper.class, ResumeMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)

public interface UserMapper {
    @Mapping(target = "resumesCount", expression = "java(user.getResumes() != null ? user.getResumes().size() : 0)")
    @Mapping(target = "vacanciesCount", expression = "java(user.getVacancies() != null ? user.getVacancies().size() : 0)")
    UserDto toDto(User user);

    User toEntity(UserDto userDto);
    User toEntity(CreateUserDto createUserDto);
}
