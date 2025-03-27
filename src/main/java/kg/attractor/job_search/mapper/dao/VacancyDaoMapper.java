package kg.attractor.job_search.mapper.dao;

import kg.attractor.job_search.model.Vacancy;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VacancyDaoMapper implements RowMapper<Vacancy> {
    @Override
    public Vacancy mapRow(ResultSet rs, int rowNum) throws SQLException {
        Vacancy vacancy = new Vacancy();
        vacancy.setId(rs.getLong("id"));
        vacancy.setName(rs.getString("name"));
        vacancy.setDescription(rs.getString("description"));
        vacancy.setCategoryId(rs.getLong("category_id"));
        vacancy.setSalary(rs.getFloat("salary"));
        vacancy.setExpFrom(rs.getInt("exp_from"));
        vacancy.setExpTo(rs.getInt("exp_to"));
        vacancy.setIsActive(rs.getBoolean("is_active"));
        vacancy.setAuthorId(rs.getLong("author_id"));
        vacancy.setCreatedDate(rs.getTimestamp("created_date"));
        vacancy.setUpdateTime(rs.getTimestamp("update_time"));
        return vacancy;
    }
}
