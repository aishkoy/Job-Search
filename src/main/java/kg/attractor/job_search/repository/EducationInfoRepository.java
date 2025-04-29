package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.EducationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationInfoRepository extends JpaRepository<EducationInfo, Long> {
}
