package kg.attractor.job_search.dao;

import kg.attractor.job_search.mapper.dao.EducationInfoDaoMapper;
import kg.attractor.job_search.models.EducationInfo;
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
public class EducationInfoDao {
    private final JdbcTemplate jdbcTemplate;

    public Optional<EducationInfo> getEducationInfoById(Long id) {
        String sql = "select * from EDUCATION_INFO where id = ?";
        EducationInfo educationInfo =  DataAccessUtils.singleResult(jdbcTemplate.query(sql, new EducationInfoDaoMapper(), id));
        return Optional.ofNullable(educationInfo);
    }
    public Long createEducationInfo(EducationInfo educationInfo) {
        String sql = """
                insert into EDUCATION_INFO(RESUME_ID, INSTITUTION, PROGRAM, START_DATE, END_DATE, DEGREE)
                values(?, ?, ?, ?, ?, ?)""";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, educationInfo.getResumeId());
            ps.setString(2, educationInfo.getInstitution());
            ps.setString(3, educationInfo.getProgram());
            ps.setDate(4, educationInfo.getStartDate());
            ps.setDate(5, educationInfo.getEndDate());
            ps.setString(6, educationInfo.getDegree());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Long updateEducationInfo(EducationInfo educationInfo) {
        String sql = """
                update EDUCATION_INFO
                set
                    INSTITUTION = ?,
                    PROGRAM = ?,
                    START_DATE = ?,
                    END_DATE = ?,
                    DEGREE = ?
                where id = ?
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, educationInfo.getInstitution());
            ps.setString(2, educationInfo.getProgram());
            ps.setDate(3, educationInfo.getStartDate());
            ps.setDate(4, educationInfo.getEndDate());
            ps.setString(5, educationInfo.getDegree());

            ps.setLong(6, educationInfo.getId());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteEducationInfo(Long id) {
        String sql = "delete from education_info where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<EducationInfo> getEducationInfosByResumeId(Long resumeId) {
        String sql = "select * from education_info where resume_id = ?";
        return jdbcTemplate.query(sql, new EducationInfoDaoMapper(),resumeId);
    }
}
