package kg.attractor.job_search.service.interfaces;

import kg.attractor.job_search.dto.MessageDto;

import java.util.List;

public interface MessageService {
    MessageDto saveMessage(MessageDto message);

    List<MessageDto> getMessagesForResponse(Long responseId);
}
