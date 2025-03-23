package kg.attractor.job_search.dao;

import kg.attractor.job_search.mapper.dao.UserDaoMapper;
import kg.attractor.job_search.models.User;
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
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

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
        String sql = "select * from users where PHONE_NUMBER = ?";
        User user = DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserDaoMapper(), phoneNumber));
        return Optional.ofNullable(user);
    }

    public Boolean existsUserByEmail(String email) {
        String sql = "select count(*) from users where email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email) > 0;
    }
    public List<User> getEmployers() {
        String sql = "select * from users where ACCOUNT_TYPE = 'employer'";
        return jdbcTemplate.query(sql, new UserDaoMapper());
    }
    public List<User> getApplicants() {
        String sql = "select * from users where ACCOUNT_TYPE = 'applicant'";
        return jdbcTemplate.query(sql, new UserDaoMapper());
    }

    public Optional<User> getEmployerById(Long id) {
        String sql = "select * from users where ACCOUNT_TYPE = 'employer' AND id = ?";
        User user = DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserDaoMapper(), id));
        return Optional.ofNullable(user);
    }

    public Optional<User> getApplicantById(Long id) {
        String sql = "select * from users where ACCOUNT_TYPE = 'applicant' AND id = ?";
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

    public Long registerUser(User user) {
        String sql = """
                INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)""";

        return saveUser(user, sql);
    }

    public Long updateUser(User user){
        String sql = """
                UPDATE users
                SET name = ?,
                    surname = ?,
                    age = ?,
                    email = ?,
                    password = ?,
                    phone_number = ?,
                    avatar = ?,
                    account_type = ?
                WHERE id = ?""";


        return saveUser(user, sql);
    }

    private Long saveUser(User user, String sql) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setInt(3, user.getAge());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getPhoneNumber());
            ps.setString(7, user.getAvatar());
            ps.setString(8, user.getAccountType());

            if (sql.toUpperCase().contains("UPDATE")) {
                ps.setLong(9, user.getId());
            }

            return ps;}, keyHolder
        );

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
