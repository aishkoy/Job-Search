package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.WorkExperienceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperienceInfo, Long> {
}
