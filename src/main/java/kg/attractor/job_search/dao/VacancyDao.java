package kg.attractor.job_search.dao;

import kg.attractor.job_search.mapper.dao.VacancyDaoMapper;
import kg.attractor.job_search.model.Vacancy;
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
public class VacancyDao {
    private final JdbcTemplate jdbcTemplate;

    public List<Vacancy> getVacanciesAppliedByUserId(Long userId) {
        String sql = """
                select * from vacancies v
                inner join responded_applicants ra on ra.vacancy_id = v.id
                inner join resumes r on ra.resume_id = r.id
                where v.is_active = true and r.applicant_id = ?
                """;
        return jdbcTemplate.query(sql, new VacancyDaoMapper(), userId);
    }

    public List<Vacancy> getActiveVacancies(){
        String sql = "select * from vacancies where IS_ACTIVE = true";
        return jdbcTemplate.query(sql, new VacancyDaoMapper());
    }

    public List<Vacancy> getVacancies(){
        String sql = "select * from vacancies";
        return jdbcTemplate.query(sql, new VacancyDaoMapper());
    }

    public List<Vacancy> getVacanciesByCategoryId(Long categoryId) {
        String sql = """
                select * from vacancies where is_active = true and category_id in
                                              (select id from CATEGORIES where id = ? or parent_id = ?)""";
        return jdbcTemplate.query(sql, new VacancyDaoMapper(), categoryId, categoryId);
    }

    public List<Vacancy> getVacanciesByCategoryName(String categoryName) {
        String sql = """
                select * from vacancies v
                inner join categories c on v.category_id = c.id
                WHERE v.is_active = true AND c.id IN (
                            SELECT id FROM categories
                            WHERE name LIKE ? OR
                                  parent_id IN
                                  (SELECT id FROM categories WHERE name LIKE ?)
                            )""";
        return jdbcTemplate.query(sql, new VacancyDaoMapper(), categoryName + "%", categoryName + "%");
    }

    public Optional<Vacancy> getVacancyById(Long vacancyId) {
        String sql = "select * from vacancies where id = ?";
        Vacancy vacancy = DataAccessUtils.singleResult(jdbcTemplate.query(sql, new VacancyDaoMapper(), vacancyId));
        return Optional.ofNullable(vacancy);
    }

    public List<Vacancy> getVacanciesByEmployerId(Long employerId) {
        String sql = "select * from vacancies where is_active = true and AUTHOR_ID = ?";
        return jdbcTemplate.query(sql, new VacancyDaoMapper(), employerId);
    }

    public Long createVacancy(Vacancy vacancy) {
        String sql = """
            INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date, update_time)
            VALUES (?, ?, ?, ?, ?, ?, true, ?, NOW(), NOW())""";

        return executeVacancyUpdate(sql, vacancy, false);
    }

    public Long updateVacancy(Vacancy vacancy) {
        String sql = """
            UPDATE vacancies
            SET
                name = ?,
                description = ?,
                category_id = ?,
                salary = ?,
                exp_from = ?,
                exp_to = ?,
                is_active = ?,
                update_time = NOW()
            WHERE id = ?""";

        return executeVacancyUpdate(sql, vacancy, true);
    }

    private Long executeVacancyUpdate(String sql, Vacancy vacancy, boolean isUpdate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, vacancy.getName());
            ps.setString(2, vacancy.getDescription());
            ps.setLong(3, vacancy.getCategoryId());
            ps.setFloat(4, vacancy.getSalary());
            ps.setInt(5, vacancy.getExpFrom());
            ps.setInt(6, vacancy.getExpTo());

            if (isUpdate) {
                ps.setBoolean(7, vacancy.getIsActive());
                ps.setLong(8, vacancy.getId());
            } else {
                ps.setLong(7, vacancy.getAuthorId());
            }

            return ps;
        }, keyHolder);

        return isUpdate ? vacancy.getId() : Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteVacancy(Long vacancyId){
        String sql = "delete from vacancies where id = ?";
        jdbcTemplate.update(sql, vacancyId);
    }
}
