package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    boolean existsByResumeIdAndVacancyId(Long resumeId, Long vacancyId);

    int countAllByResume_Applicant_Id(Long applicantId);

    int countAllByIsConfirmedTrueAndResume_Applicant_Id(Long applicantId);

    int countAllByIsConfirmedTrueAndVacancy_Employer_Id(Long employerId);

    int countAllByVacancy_Employer_Id(Long employerId);

    Page<Response> findAllByResume_Applicant_Id(Long applicantId, Pageable pageable);

    Page<Response> findAllByVacancy_Id(Long vacancyId, Pageable pageable);

    boolean existsByVacancy_IdAndResume_Applicant_Id(Long vacancyId, Long applicantId);
}
