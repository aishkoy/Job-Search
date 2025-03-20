package kg.attractor.job_search.dao;

import kg.attractor.job_search.mapper.dao.VacancyDaoMapper;
import kg.attractor.job_search.models.Vacancy;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
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
                where r.applicant_id = ?
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
        String sql = "select * from vacancies where category_id = ?";
        return jdbcTemplate.query(sql, new VacancyDaoMapper(), categoryId);
    }

    public List<Vacancy> getVacanciesByCategoryName(String categoryName) {
        String sql = """
                select * from vacancies v
                inner join categories c on v.category_id = c.id
                where c.NAME like ?""";
        return jdbcTemplate.query(sql, new VacancyDaoMapper(), categoryName + "%");
    }

    public Optional<Vacancy> getVacancyById(Long vacancyId) {
        String sql = "select * from vacancies where id = ?";
        Vacancy vacancy = jdbcTemplate.queryForObject(sql, new VacancyDaoMapper(), vacancyId);
        return Optional.ofNullable(vacancy);
    }

    public List<Vacancy> getVacanciesByEmployerId(Long employerId) {
        String sql = "select * from vacancies where AUTHOR_ID = ?";
        return jdbcTemplate.query(sql, new VacancyDaoMapper(), employerId);
    }
}
