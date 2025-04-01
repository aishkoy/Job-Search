package kg.attractor.job_search.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDao {
    private final JdbcTemplate jdbcTemplate;

    public Long getRoleById(Long id) {
        String sql = "select id from roles where id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, id);
    }
}
