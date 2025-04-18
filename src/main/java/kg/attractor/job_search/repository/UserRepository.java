package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    List<User> findAllByName(String name);

    @Query("select u from User u where u.role.role = 'APPLICANT'")
    List<User> findApplicants();

    @Query("select u from User u where u.role.role = 'APPLICANT' and u.id = :userId")
    Optional<User> findApplicantById(@Param("userId") Long userId);

    @Query("select u from User u where u.role.role = 'EMPLOYER'")
    List<User> findEmployers();

    @Query("select u from User u where u.role.role = 'EMPLOYER' and u.id = :userId")
    Optional<User> findEmployerById(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("update User u set u.avatar = :avatar where u.id = :userId")
    void updateUserAvatar(@Param("userId") Long userId, @Param("avatar") String avatar);

    @Query("""
            select u from User u
            inner join Resume r on u.id = r.applicant.id
            inner join RespondedApplicant ra on ra.resume.id = r.id
            where ra.vacancy.id = :vacancyId""")
    List<User> findApplicantsByVacancyId(@Param("vacancyId")  Long vacancyId);

    boolean existsByEmail(String email);
}
