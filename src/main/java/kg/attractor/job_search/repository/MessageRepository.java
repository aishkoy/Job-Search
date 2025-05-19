package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByResponseIdOrderByTimestampAsc(Long responseId);

    @Query("""
                SELECT COUNT(m)
                FROM Message m
                JOIN m.response r
                JOIN r.vacancy v
                JOIN r.resume res
                WHERE m.isRead = false
                  AND m.user.id <> :userId
                  AND (
                    v.employer.id = :userId OR
                    res.applicant.id = :userId
                  )
            """)
    int countUnreadMessagesForUser(@Param("userId") Long userId);

    @Query("""
                SELECT COUNT(m)
                FROM Message m
                JOIN m.response r
                JOIN r.vacancy v
                WHERE m.isRead = false
                  AND m.user.id <> :userId
                  AND v.id = :vacancyId
            """)
    int countUnreadMessagesByVacancy(@Param("vacancyId") Long vacancyId,
                                     @Param("userId") Long userId);


    @Query("""
                SELECT COUNT(m)
                FROM Message m
                JOIN m.response r
                JOIN r.resume res
                WHERE m.isRead = false
                  AND m.user.id <> :userId
                  AND res.id = :resumeId
            """)
    int countUnreadMessagesByResume(@Param("resumeId") Long resumeId,
                                    @Param("userId") Long userId);


    @Query("""
                SELECT COUNT(m)
                FROM Message m
                WHERE m.response.id = :responseId
                  AND m.user.id <> :userId
                  AND m.isRead = false
            """)
    int countUnreadMessagesInRoom(@Param("responseId") Long responseId,
                                  @Param("userId") Long userId);
}
