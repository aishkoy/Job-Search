package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.RoleDto;
import kg.attractor.job_search.entity.Role;
import kg.attractor.job_search.exception.RoleNotFoundException;
import kg.attractor.job_search.mapper.RoleMapper;
import kg.attractor.job_search.repository.RoleRepository;
import kg.attractor.job_search.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id).
                orElseThrow(() -> new RoleNotFoundException("Не существует роли с таким id!"));
        return roleMapper.toDto(role);
    }
}
