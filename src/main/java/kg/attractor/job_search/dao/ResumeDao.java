package kg.attractor.job_search.dao;

import kg.attractor.job_search.mapper.dao.ResumeDaoMapper;
import kg.attractor.job_search.models.Resume;
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
public class ResumeDao {
    private final JdbcTemplate jdbcTemplate;

    public List<Resume> getResumes(){
        String sql = "select * from resumes";
        return jdbcTemplate.query(sql, new ResumeDaoMapper());
    }

    public List<Resume> getActiveResumes(){
        String sql = "select * from RESUMES where IS_ACTIVE = true";
        return jdbcTemplate.query(sql, new ResumeDaoMapper());
    }

    public List<Resume> getResumesByCategoryId(Long categoryId) {
        String sql = """
                select * from resumes where is_active = true and category_id in
                                            (select id from CATEGORIES where id = ? or parent_id = ?)""";
        return jdbcTemplate.query(sql, new ResumeDaoMapper(), categoryId, categoryId);
    }

    public List<Resume> getResumesByApplicantId(Long userId) {
        String sql = "select * from resumes where is_active = true and applicant_id = ?";
        return jdbcTemplate.query(sql, new ResumeDaoMapper(), userId);
    }

    public List<Resume> getResumesByApplicantName(String applicantName) {
        String sql = """
                select * from resumes r
                inner join users u on r.applicant_id = u.id
                where r.is_active = true and u.name like ?""";
        return jdbcTemplate.query(sql, new ResumeDaoMapper(), applicantName + "%");
    }

    public Optional<Resume> getResumeById(Long id) {
        String sql = "select * from resumes where id = ?";
        Resume resume = DataAccessUtils.singleResult(
                jdbcTemplate.query(sql, new ResumeDaoMapper(), id)
        );
        return Optional.ofNullable(resume);
    }

    public Long createResume(Resume resume) {
        String sql = """
        INSERT INTO resumes (name, category_id, salary, is_active, applicant_id, created_date, update_time)
        VALUES (?, ?, ?, ?, ?, NOW(), NOW())""";

        return saveResume(sql, resume, false);
    }

    public Long updateResume(Resume resume) {
        String sql = """
        UPDATE resumes
        SET
            name = ?,
            category_id = ?,
            salary = ?,
            is_active = ?,
            update_time = NOW()
        WHERE id = ?""";

        return saveResume(sql, resume, true);
    }

    private Long saveResume(String sql, Resume resume, boolean isUpdate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, resume.getName());
            ps.setLong(2, resume.getCategoryId());
            ps.setFloat(3, resume.getSalary());
            ps.setBoolean(4, resume.getIsActive());

            if (isUpdate) {
                ps.setLong(5, resume.getId());
            } else{
                ps.setLong(5, resume.getApplicantId());
            }

            return ps;
        }, keyHolder);

        return isUpdate ? resume.getId() : Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
    public void deleteResume(Long resumeId){
        String sql = "delete from resumes where id = ?";
        jdbcTemplate.update(sql, resumeId);
    }
}
