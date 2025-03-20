package kg.attractor.job_search.mapper.dao;

import kg.attractor.job_search.models.Vacancy;
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
        vacancy.setCategoryId(rs.getLong("categoryId"));
        vacancy.setSalary(rs.getFloat("salary"));
        vacancy.setExpFrom(rs.getInt("expFrom"));
        vacancy.setExpTo(rs.getInt("expTo"));
        vacancy.setIsActive(rs.getBoolean("isActive"));
        vacancy.setAuthorId(rs.getLong("authorId"));
        vacancy.setCreatedDate(rs.getTimestamp("createdDate"));
        vacancy.setUpdateTime(rs.getTimestamp("updateTime"));
        return vacancy;
    }
}
