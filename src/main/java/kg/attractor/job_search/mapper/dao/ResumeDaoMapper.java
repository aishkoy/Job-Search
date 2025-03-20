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
        resume.setApplicantId(rs.getLong("applicantId"));
        resume.setCategoryId(rs.getLong("categoryId"));
        resume.setSalary(rs.getFloat("salary"));
        resume.setIsActive(rs.getBoolean("isActive"));
        resume.setCreatedDate(rs.getTimestamp("createdDate"));
        resume.setUpdateTime(rs.getTimestamp("updateTime"));
        return resume;
    }
}
