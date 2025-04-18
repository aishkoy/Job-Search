package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findAllByIsActiveTrue();
    List<Resume> findAllByApplicantId(Long applicantId);
    List<Resume> findAllByApplicantName(String applicantName);
    List<Resume> findAllByCategoryId(Long categoryId);
    boolean existsByIdAndApplicantId(Long resumeId, Long applicantId);
}
