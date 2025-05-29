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
    Optional<Vacancy> findByIdAndEmployerId(Long vacancyId, Long employerId);

    @Query(value = "SELECT * FROM VACANCIES ORDER BY created_at DESC LIMIT :limit",
            nativeQuery = true)
    List<Vacancy> findLastVacancies(@Param("limit") Integer limit);

    boolean existsByIdAndEmployerId(Long vacancyId, Long employerId);

    Page<Vacancy> findAllByIsActiveTrue(Pageable pageable);

    @Query("SELECT v FROM Vacancy v WHERE v.isActive = true AND " +
            "(LOWER(v.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(v.employer.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Vacancy> findAllByIsActiveTrueWithQuery(@Param("query") String query, Pageable pageable);

    @Query("SELECT v FROM Vacancy v WHERE v.isActive = true AND v.category.id IN :categoryIds")
    Page<Vacancy> findAllByCategoryIds(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    @Query("SELECT v FROM Vacancy v WHERE v.isActive = true AND v.category.id IN :categoryIds AND " +
            "(LOWER(v.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(v.employer.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Vacancy> findAllByCategoryIdsAndQuery(
            @Param("query") String query,
            @Param("categoryIds") List<Long> categoryIds,
            Pageable pageable);


    Page<Vacancy> findAllByEmployerId(Long employerId, Pageable pageable);
}
