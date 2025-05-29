package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    boolean existsByIdAndApplicantId(Long resumeId, Long applicantId);

    Page<Resume> findAllByIsActiveTrue(Pageable pageable);

    @Query("SELECT r FROM Resume r WHERE r.isActive = true AND " +
            "(LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(r.applicant.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Resume> findAllActiveWithQuery(String query, Pageable pageable);

    @Query("SELECT r FROM Resume r WHERE r.isActive = true AND r.category.id IN :categoryIds")
    Page<Resume> findAllByCategoryIds(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    @Query("SELECT r FROM Resume r WHERE r.isActive = true AND r.category.id IN :categoryIds AND " +
            "(LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(r.applicant.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Resume> findAllByCategoryIdsAndQuery(
            @Param("query") String query,
            @Param("categoryIds") List<Long> categoryIds,
            Pageable pageable);

    Page<Resume> findAllByApplicantId(Long applicantId, Pageable pageable);

    @Query(value = "SELECT * FROM RESUMES ORDER BY created_at DESC LIMIT :limit",
            nativeQuery = true)
    List<Resume> findLastResumes(@Param("limit") Integer limit);
}
