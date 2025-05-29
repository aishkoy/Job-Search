package kg.attractor.job_search.service.interfaces;

import kg.attractor.job_search.dto.RoleDto;

import java.util.List;

public interface RoleService {
    RoleDto getRoleById(Long id);

    List<RoleDto> getAllRoles();
}
