package kg.attractor.job_search.dao;

import kg.attractor.job_search.mapper.dao.UserDaoMapper;
import kg.attractor.job_search.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public List<User> getUsers() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, new UserDaoMapper());
    }

    public Optional<User> getUserById(Long id) {
        String sql = "select * from users where id = ?";
        User user = DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserDaoMapper(), id));
        return Optional.ofNullable(user);
    }
    public List<User> getUsersByName(String name) {
        String sql = "select * from users where name  like ?";
        return jdbcTemplate.query(sql, new UserDaoMapper(), name + "%");
    }

    public Optional<User> getUserByEmail(String email) {
        String sql = "select * from users where email = ?";
        User user = DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserDaoMapper(), email));
        return Optional.ofNullable(user);
    }

    public Optional<User> getUserByPhone(String phoneNumber) {
        String sql = "select * from users where PHONE_NUMBER like ?";
        User user = DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserDaoMapper(), phoneNumber));
        return Optional.ofNullable(user);
    }

    public Boolean existsUserByEmail(String email) {
        String sql = "select count(*) from users where email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email) > 0;
    }
    public List<User> getEmployers() {
        String sql = "select * from users where role_id = (select id from roles where role like 'EMPLOYER')";
        return jdbcTemplate.query(sql, new UserDaoMapper());
    }
    public List<User> getApplicants() {
        String sql = "select * from users where role_id = (select id from roles where role like 'APPLICANT')";
        return jdbcTemplate.query(sql, new UserDaoMapper());
    }

    public Optional<User> getEmployerById(Long id) {
        String sql = "select * from users where role_id = (select id from roles where role like 'EMPLOYER') AND id = ?";
        User user = DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserDaoMapper(), id));
        return Optional.ofNullable(user);
    }

    public Optional<User> getApplicantById(Long id) {
        String sql = "select * from users where role_id = (select id from roles where role like 'APPLICANT') AND id = ?";
        User user = DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserDaoMapper(), id));
        return Optional.ofNullable(user);
    }

    public List<User> getApplicantsByVacancyId(Long vacancyId) {
        String sql = """
                select * from users u
                inner join resumes r on u.id = r.applicant_id
                inner join responded_applicants ra on ra.resume_id = r.id
                where ra.vacancy_id = ?""";
        return jdbcTemplate.query(sql, new UserDaoMapper(), vacancyId);
    }

    public String getUserName(Long userId) {
        String sql = "select concat(name, ' ', surname) from users where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, userId);
    }

    public Long registerUser(User user) {
        String sql = """
            INSERT INTO users (name, surname, phone_number, avatar, age, email, password, role_id, enabled)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, true)""";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setString(3, user.getPhoneNumber());
            ps.setString(4, user.getAvatar());

            if (user.getAge() == null) {
                ps.setNull(5, Types.INTEGER);
            } else {
                ps.setInt(5, user.getAge());
            }
            ps.setString(6, user.getEmail());
            ps.setString(7, user.getPassword());
            ps.setLong(8, user.getRoleId());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Long updateUser(User user) {
        String sql = """
            UPDATE users
            SET name = ?,
                surname = ?,
                phone_number = ?
            WHERE id = ?""";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setString(3, user.getPhoneNumber());
            ps.setLong(4, user.getId());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Long updateUserAvatar(Long userId, String avatar) {
        String sql = "UPDATE users SET avatar = ? WHERE id = ?";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, avatar);
            ps.setLong(2, userId);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteUser(Long userId){
        String sql = "delete from users where id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
