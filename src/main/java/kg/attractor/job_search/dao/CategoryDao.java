package kg.attractor.job_search.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryDao {
    private final JdbcTemplate jdbcTemplate;

    public Optional<Long> getCategoryById(Long id){
        String sql  = "select id from categories where id = ? ";
        Long categoryId = jdbcTemplate.queryForObject(sql, Long.class, id);
        return Optional.ofNullable(categoryId);
    }
}
