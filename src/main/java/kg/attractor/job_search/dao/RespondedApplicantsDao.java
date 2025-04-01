package kg.attractor.job_search.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RespondedApplicantsDao {
    private final JdbcTemplate jdbcTemplate;

    public Long respond(Long resumeId, Long vacancyId) {
        String sql = "insert into responded_applicants (resume_id, vacancy_id, confirmation) values (?, ?, false)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, resumeId);
            ps.setLong(2, vacancyId);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public boolean hasResponded(Long resumeId, Long vacancyId) {
        String sql = "SELECT COUNT(*) FROM responded_applicants WHERE resume_id = ? AND vacancy_id = ?";

        Integer count = jdbcTemplate.queryForObject(
                sql,
                new Object[]{resumeId, vacancyId},
                Integer.class
        );

        return count != null && count > 0;
    }
}
