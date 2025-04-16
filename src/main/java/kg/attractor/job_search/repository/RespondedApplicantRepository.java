package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.RespondedApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespondedApplicantRepository extends JpaRepository<RespondedApplicant, Long> {
    boolean existsByResumeIdAndVacancyId(Long resumeId, Long vacancyId);
}
