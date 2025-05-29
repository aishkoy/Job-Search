package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByResponseIdOrderByTimestampAsc(Long responseId);
}
