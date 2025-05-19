package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    boolean existsByResumeIdAndVacancyId(Long resumeId, Long vacancyId);

    int countAllByResume_Applicant_Id(Long applicantId);

    int countAllByIsConfirmedTrueAndResume_Applicant_Id(Long applicantId);

    int countAllByIsConfirmedTrueAndVacancy_Employer_Id(Long employerId);

    int countAllByVacancy_Employer_Id(Long employerId);

    Page<Response> findAllByResume_Applicant_Id(Long applicantId, Pageable pageable);

    Page<Response> findAllByVacancy_Id(Long vacancyId, Pageable pageable);

    Page<Response> findAllByResumeId(Long resumeId, Pageable pageable);

    boolean existsByVacancy_IdAndResume_Applicant_Id(Long vacancyId, Long applicantId);

    @Query("""
            select re from Response re 
            where re.vacancy.id =: vacancyId 
            and ( re.vacancy.employer.id =: userId 
                or re.resume.applicant.id =:userId )""")
    Optional<Response> findByVacancyIdAndUserId(@Param("vacancyId") Long vacancyId,
                                                @Param("userId") Long userId);
}
