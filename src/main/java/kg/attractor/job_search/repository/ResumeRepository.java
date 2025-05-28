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
    List<Resume> findAllByIsActiveTrue();

    List<Resume> findAllByApplicantId(Long applicantId);

    List<Resume> findAllByApplicantName(String applicantName);

    List<Resume> findAllByCategoryId(Long categoryId);

    boolean existsByIdAndApplicantId(Long resumeId, Long applicantId);

    Page<Resume> findAllByIsActiveTrue(Pageable pageable);

    Page<Resume> findAllByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);

    Page<Resume> findAllByApplicantId(Long applicantId, Pageable pageable);

    @Query(value = "SELECT * FROM RESUMES ORDER BY created_at DESC LIMIT :limit",
            nativeQuery = true)
    List<Resume> findLastResumes(@Param("limit") Integer limit);
}
