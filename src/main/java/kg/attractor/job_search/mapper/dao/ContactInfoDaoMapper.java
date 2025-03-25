package kg.attractor.job_search.mapper.dao;

import kg.attractor.job_search.models.ContactInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactInfoDaoMapper implements RowMapper<ContactInfo> {
    @Override
    public ContactInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(rs.getLong("id"));
        contactInfo.setResumeId(rs.getLong("resume_id"));
        contactInfo.setTypeId(rs.getLong("type_id"));
        contactInfo.setValue(rs.getString("value"));
        return contactInfo;
    }
}
