package kg.attractor.job_search.dao;

import kg.attractor.job_search.mapper.dao.ContactInfoDaoMapper;
import kg.attractor.job_search.model.ContactInfo;
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
public class ContactInfoDao {
    private final JdbcTemplate jdbcTemplate;

    public List<ContactInfo> getContactInfoByResumeId(Long resumeId) {
        String sql = "select * from CONTACTS_INFO where resume_id = ?";
        return jdbcTemplate.query(sql, new ContactInfoDaoMapper(), resumeId);
    }

    public Optional<ContactInfo> getContactInfoById(Long id){
        String sql = "select * from CONTACTS_INFO where id = ?";
        ContactInfo contactInfo = DataAccessUtils.singleResult(jdbcTemplate.query(sql, new ContactInfoDaoMapper(), id));
        return Optional.ofNullable(contactInfo);
    }

    public Long createContactInfo(ContactInfo contactInfo) {
        String sql = "insert into CONTACTS_INFO(TYPE_ID, RESUME_ID, CONTACT_VALUE) values(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, contactInfo.getTypeId());
            ps.setLong(2, contactInfo.getResumeId());
            ps.setString(3, contactInfo.getContactValue());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Long updateContactInfo(ContactInfo contactInfo) {
        String sql = """
            UPDATE CONTACTS_INFO
            SET
                TYPE_ID = ?,
                RESUME_ID = ?,
                CONTACT_VALUE = ?
            WHERE id = ?""";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, contactInfo.getTypeId());
            ps.setLong(2, contactInfo.getResumeId());
            ps.setString(3, contactInfo.getContactValue());
            ps.setLong(4, contactInfo.getId());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteContactInfo(Long id) {
        String sql = "delete from CONTACTS_INFO where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
