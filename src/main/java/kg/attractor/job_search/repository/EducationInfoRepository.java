package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.EducationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationInfoRepository extends JpaRepository<EducationInfo, Long> {
    List<EducationInfo> findAllByResumeId(Long resumeId);
}
