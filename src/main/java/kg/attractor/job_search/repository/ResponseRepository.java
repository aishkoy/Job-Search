package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    boolean existsByResumeIdAndVacancyId(Long resumeId, Long vacancyId);
}
