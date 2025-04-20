package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.RoleDto;
import kg.attractor.job_search.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleDto roleDto);
    RoleDto toDto(Role role);
}
