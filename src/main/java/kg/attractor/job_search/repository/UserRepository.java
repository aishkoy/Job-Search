package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByResetPasswordToken(String token);

    Page<User> findAllByNameStartingWithOrSurnameStartingWith(String name, String surname, Pageable pageable);

    @Query("select u from User u where u.role.name = 'APPLICANT'")
    Page<User> findApplicants(Pageable pageable);

    @Query("select u from User u where u.role.name = 'APPLICANT' and u.id = :userId")
    Optional<User> findApplicantById(@Param("userId") Long userId);

    @Query("select u from User u where u.role.name = 'EMPLOYER'")
    Page<User> findEmployers(Pageable pageable);

    @Query("select u from User u where u.role.name = 'EMPLOYER' and u.id = :userId")
    Optional<User> findEmployerById(@Param("userId") Long userId);

    @Query("SELECT u.preferredLanguage FROM User u WHERE u.email = :email")
    Optional<String> findPreferredLanguageByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.preferredLanguage = :language WHERE u.email = :email")
    void updateUserLanguage(String email, String language);

    @Modifying
    @Transactional
    @Query("update User u set u.avatar = :avatar where u.id = :userId")
    void updateUserAvatar(@Param("userId") Long userId, @Param("avatar") String avatar);

    boolean existsByEmail(String email);

    @Query("select u from User u where u.role.name = 'EMPLOYER'")
    Page<User> findEmployerPage(Pageable pageable);

    @Query("""
            select u from User u where u.role.name = 'EMPLOYER' AND 
            (LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%')) OR 
            LOWER(u.surname) LIKE LOWER(CONCAT('%', :query, '%')))""")
    Page<User> findEmployerPageWithQuery(@Param("query") String query, Pageable pageable);

    @Query("select u from User u where u.role.name = 'APPLICANT'")
    Page<User> findApplicantPage(Pageable pageable);

    @Query("""
            select u from User u where u.role.name = 'APPLICANT' AND 
            (LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%')) OR 
            LOWER(u.surname) LIKE LOWER(CONCAT('%', :query, '%')))
            """)
    Page<User> findApplicantPageWithQuery(@Param("query") String query, Pageable pageable);
}
