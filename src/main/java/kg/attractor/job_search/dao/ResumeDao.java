package kg.attractor.job_search.dao;

import kg.attractor.job_search.mapper.dao.ResumeDaoMapper;
import kg.attractor.job_search.models.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ResumeDao {
    private final JdbcTemplate jdbcTemplate;
    public List<Resume> getResumesByCategoryId(Long categoryId) {
        String sql = "select * from resumes where category_id = ?";
        return jdbcTemplate.query(sql, new ResumeDaoMapper(), categoryId);
    }

    public List<Resume> getResumesByApplicantId(Long userId) {
        String sql = "select * from resumes where applicant_id = ?";
        return jdbcTemplate.query(sql, new ResumeDaoMapper(), userId);
    }

    public List<Resume> getResumesByApplicantName(String applicantName) {
        String sql = """
                select * from resumes r
                inner join users u on r.applicant_id = u.id
                where u.name like ?""";
        return jdbcTemplate.query(sql, new ResumeDaoMapper(), applicantName + "%");
    }

    public Optional<Resume> getResumeById(Long id) {
        String sql = "select * from resumes where id = ?";
        Resume resume = jdbcTemplate.queryForObject(sql, new ResumeDaoMapper(), id);
        return Optional.ofNullable(resume);
    }
}
