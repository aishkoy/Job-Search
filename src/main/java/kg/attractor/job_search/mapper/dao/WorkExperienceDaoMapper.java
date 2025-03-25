package kg.attractor.job_search.mapper.dao;

import kg.attractor.job_search.models.WorkExperienceInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkExperienceDaoMapper implements RowMapper<WorkExperienceInfo> {
    @Override
    public WorkExperienceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        WorkExperienceInfo workExperienceInfo = new WorkExperienceInfo();
        workExperienceInfo.setId(rs.getLong("id"));
        workExperienceInfo.setResumeId(rs.getLong("resume_id"));
        workExperienceInfo.setYears(rs.getInt("years"));
        workExperienceInfo.setCompanyName(rs.getString("company_name"));
        workExperienceInfo.setPosition(rs.getString("position"));
        workExperienceInfo.setResponsibilities(rs.getString("responsibilities"));
        return workExperienceInfo;
    }
}
