package kg.attractor.job_search.dao;

import kg.attractor.job_search.mapper.dao.UserDaoMapper;
import kg.attractor.job_search.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    public List<User> getUsers(){
        String sql = "select * from users";
        return jdbcTemplate.query(sql, new UserDaoMapper());
    }

    public Optional<User> getUserById(Long id){
        String sql = "select * from users where id = ?";
        User user = jdbcTemplate.queryForObject(sql, new UserDaoMapper(), id);
        return Optional.ofNullable(user);
    }

    public List<User> getUsersByName(String name){
        String sql = "select * from users where name = ?";
        return jdbcTemplate.query(sql, new UserDaoMapper(), name);
    }

    public Optional<User> getUserByEmail(String email){
        String sql = "select * from users where email = ?";
        User user = jdbcTemplate.queryForObject(sql, new UserDaoMapper(), email);
        return Optional.ofNullable(user);
    }

    public Optional<User> getUserByPhone(String phone){
        String sql = "select * from users where phone = ?";
        User user = jdbcTemplate.queryForObject(sql, new UserDaoMapper(), phone);
        return Optional.ofNullable(user);
    }
}
