package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.WorkExperienceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperienceInfo, Long> {
    List<WorkExperienceInfo> findAllByResumeId(Long resumeId);
}
