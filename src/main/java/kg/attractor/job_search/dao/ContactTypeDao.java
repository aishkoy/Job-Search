package kg.attractor.job_search.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContactTypeDao {
    private final JdbcTemplate jdbcTemplate;

    public Long getContactTypeById(Long id) {
        String sql = "select id from CONTACT_TYPES where id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, id);
    }
}
