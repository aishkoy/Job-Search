package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.MessageDto;
import kg.attractor.job_search.dto.ResponseDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.entity.Message;
import kg.attractor.job_search.entity.Response;
import kg.attractor.job_search.entity.User;
import kg.attractor.job_search.mapper.MessageMapper;
import kg.attractor.job_search.repository.MessageRepository;
import kg.attractor.job_search.service.interfaces.MessageService;
import kg.attractor.job_search.service.interfaces.ResponseService;
import kg.attractor.job_search.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ResponseService responseService;
    private final UserService userService;
    private final MessageMapper messageMapper;

    @Override
    public int getUnreadMessagesCount(Long userId) {
        return messageRepository.countUnreadMessagesForUser(userId);
    }

    @Override
    public int getUnreadMessagesCountByVacancy(Long vacancyId, Long userId) {
        return messageRepository.countUnreadMessagesByVacancy(vacancyId, userId);
    }

    @Override
    public int getUnreadMessagesCountByResponse(Long responseId, Long userId) {
        return messageRepository.countUnreadMessagesInRoom(responseId, userId);
    }

    @Override
    public int getUnreadMessagesCountByResume(Long resumeId, Long userId) {
        return messageRepository.countUnreadMessagesByResume(resumeId, userId);
    }

    public List<MessageDto> getMessagesForResponse(Long responseId) {
        return messageRepository.findAllByResponseIdOrderByTimestampAsc(responseId)
                .stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void markMessageAsRead(Long messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.setIsRead(true);
            messageRepository.save(message);
        });
    }

    @Override
    @Transactional
    public MessageDto saveMessage(MessageDto dto) {
        log.info("Сохранение сообщения для отклика с ID: {}", dto.getResponse().getId());

        try {
            Message message = new Message();
            message.setContent(dto.getContent());
            message.setTimestamp(dto.getTimestamp());
            message.setIsRead(dto.getIsRead());

            Response response = responseService.getEntityById(dto.getResponse().getId());
            User user = userService.getEntityById(dto.getUser().getId());

            message.setResponse(response);
            message.setUser(user);

            Message savedMessage = messageRepository.save(message);

            MessageDto result = new MessageDto();
            result.setId(savedMessage.getId());
            result.setContent(savedMessage.getContent());
            result.setTimestamp(savedMessage.getTimestamp());
            result.setIsRead(savedMessage.getIsRead());
            result.setResponse(ResponseDto.builder().id(response.getId()).build());
            result.setUser(UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .build());

            return result;
        } catch (Exception e) {
            log.error("Ошибка при сохранении сообщения: {}", e.getMessage(), e);
            throw e;
        }
    }
}
