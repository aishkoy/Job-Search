package kg.attractor.job_search.mapper.dao;

import kg.attractor.job_search.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setAge(rs.getInt("age"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhoneNumber(rs.getString("phoneNumber"));
        user.setAvatar(rs.getString("avatar"));
        user.setAccountType(rs.getString("accountType"));
        return user;
    }
}
