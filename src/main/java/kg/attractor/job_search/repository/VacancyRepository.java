package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    List<Vacancy> findAllByIsActiveTrue();
    List<Vacancy> findAllByCategoryId(Long id);
    Optional<Vacancy> findByIdAndEmployerId(Long vacancyId, Long employerId);
    List<Vacancy> findAllByEmployerId(Long employerId);
    List<Vacancy> findAllByCategoryName(String categoryName);
    List<Vacancy> findAllByOrderByCreatedAtDesc();

    boolean existsByIdAndEmployerId(Long vacancyId, Long employerId);

    @Query("SELECT v FROM Vacancy v " +
            "JOIN RespondedApplicant ra ON ra.vacancy.id = v.id " +
            "JOIN Resume r ON ra.resume.id = r.id " +
            "WHERE r.applicant.id = :applicantId")
    List<Vacancy> findVacanciesAppliedByUserId(Long applicantId);
}
