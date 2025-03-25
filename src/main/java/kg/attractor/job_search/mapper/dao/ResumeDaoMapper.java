package kg.attractor.job_search.mapper.dao;

import kg.attractor.job_search.models.Resume;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResumeDaoMapper implements RowMapper<Resume> {
    @Override
    public Resume mapRow(ResultSet rs, int rowNum) throws SQLException {
        Resume resume = new Resume();
        resume.setId(rs.getLong("id"));
        resume.setName(rs.getString("name"));
        resume.setApplicantId(rs.getLong("applicant_id"));
        resume.setCategoryId(rs.getLong("category_id"));
        resume.setSalary(rs.getFloat("salary"));
        resume.setIsActive(rs.getBoolean("is_active"));
        resume.setCreatedDate(rs.getTimestamp("created_date"));
        resume.setUpdateTime(rs.getTimestamp("update_time"));
        return resume;
    }
}
