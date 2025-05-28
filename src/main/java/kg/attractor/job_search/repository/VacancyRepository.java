package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    List<Vacancy> findAllByIsActiveTrue();

    List<Vacancy> findAllByCategoryId(Long id);

    Optional<Vacancy> findByIdAndEmployerId(Long vacancyId, Long employerId);

    List<Vacancy> findAllByCategoryName(String categoryName);

    @Query(value = "SELECT * FROM VACANCIES ORDER BY created_at DESC LIMIT :limit",
            nativeQuery = true)
    List<Vacancy> findLastVacancies(@Param("limit") Integer limit);

    @Query("SELECT v FROM Vacancy v " +
            "JOIN Response ra ON ra.vacancy.id = v.id " +
            "JOIN Resume r ON ra.resume.id = r.id " +
            "WHERE r.applicant.id = :applicantId")
    List<Vacancy> findVacanciesAppliedByUserId(Long applicantId);


    boolean existsByIdAndEmployerId(Long vacancyId, Long employerId);


    Page<Vacancy> findAllByIsActiveTrue(Pageable pageable);

    @Query("SELECT v FROM Vacancy v WHERE v.isActive = true AND " +
            "(LOWER(v.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(v.employer.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Vacancy> findAllByIsActiveTrueWithQuery(@Param("query") String query, Pageable pageable);

    Page<Vacancy> findAllByCategoryIdAndIsActiveTrue(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT v FROM Vacancy v WHERE v.isActive = true AND v.category.id = :categoryId AND " +
            "(LOWER(v.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(v.employer.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Vacancy> findAllByCategoryIdAndIsActiveTrueWithQuery(
            @Param("query") String query,
            @Param("categoryId") Long categoryId,
            Pageable pageable);


    Page<Vacancy> findAllByEmployerId(Long employerId, Pageable pageable);
}
