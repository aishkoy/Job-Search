package kg.attractor.job_search.service.interfaces;

import kg.attractor.job_search.dto.MessageDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageService {
    @Transactional
    void markMessageAsRead(Long messageId);

    MessageDto saveMessage(MessageDto message);

    int getUnreadMessagesCount(Long userId);

    int getUnreadMessagesCountByVacancy(Long vacancyId, Long userId);

    int getUnreadMessagesCountByResponse(Long responseId, Long userId);

    int getUnreadMessagesCountByResume(Long resumeId, Long userId);

    List<MessageDto> getMessagesForResponse(Long responseId);
}
