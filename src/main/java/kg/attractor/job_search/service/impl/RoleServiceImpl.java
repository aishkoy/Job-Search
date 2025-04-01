package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.RoleDao;
import kg.attractor.job_search.exception.RoleNotFoundException;
import kg.attractor.job_search.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleDao dao;

    @Override
    public Long getRoleId(Long id) {
        try {
            return dao.getRoleById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RoleNotFoundException("Не существует роли с таким id!");
        }
    }
}
