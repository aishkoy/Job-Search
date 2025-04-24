package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.dto.user.SimpleUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {VacancyMapper.class, ResumeMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)

public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toEntity(CreateUserDto createUserDto);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    User toEntity(SimpleUserDto simpleUserDto);

    SimpleUserDto toSimpleUserDto(UserDto user);
}
