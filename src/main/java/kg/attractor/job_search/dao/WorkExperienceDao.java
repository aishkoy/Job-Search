package kg.attractor.job_search.dao;

import kg.attractor.job_search.mapper.dao.WorkExperienceDaoMapper;
import kg.attractor.job_search.models.WorkExperienceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WorkExperienceDao {
    private final JdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    public Optional<WorkExperienceInfo> getWorkExperienceInfoById(Long id) {
        String sql = "select * from WORK_EXPERIENCE_INFO where id = ?";
        WorkExperienceInfo workExperienceInfo = DataAccessUtils.singleResult(jdbcTemplate.query(sql, new WorkExperienceDaoMapper(), id));
        return Optional.ofNullable(workExperienceInfo);
    }

    public Long createWorkExperience(WorkExperienceInfo workExperience){
        String sql = """
                insert into WORK_EXPERIENCE_INFO(RESUME_ID, YEARS, COMPANY_NAME, POSITION, RESPONSIBILITIES)
                values(?, ?, ?, ?, ?)""";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, workExperience.getResumeId());
            ps.setInt(2, workExperience.getYears());
            ps.setString(3, workExperience.getCompanyName());
            ps.setString(4, workExperience.getPosition());
            ps.setString(5, workExperience.getResponsibilities());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Long updateWorkExperience(WorkExperienceInfo workExperience){
        String sql = """
                update WORK_EXPERIENCE_INFO
                set
                    YEARS = ?,
                    COMPANY_NAME = ?,
                    POSITION = ?,
                    RESPONSIBILITIES = ?
                where id = ?
                """;

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, workExperience.getYears());
            ps.setString(2, workExperience.getCompanyName());
            ps.setString(3, workExperience.getPosition());
            ps.setString(4, workExperience.getResponsibilities());

            ps.setLong(5, workExperience.getId());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteWorkExperience(Long id) {
        String sql = "delete from WORK_EXPERIENCE_INFO where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<WorkExperienceInfo> getWorkExperienceInfoByResumeId(Long resumeId) {
        String sql = "select * from WORK_EXPERIENCE_INFO where RESUME_ID = ?";
        return jdbcTemplate.query(sql, new WorkExperienceDaoMapper(), resumeId);
    }
}
