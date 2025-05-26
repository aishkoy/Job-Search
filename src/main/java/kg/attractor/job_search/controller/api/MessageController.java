package kg.attractor.job_search.controller.api;

import kg.attractor.job_search.dto.MessageDto;
import kg.attractor.job_search.service.interfaces.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/responses/{responseId}")
    public ResponseEntity<List<MessageDto>> getMessages(@PathVariable Long responseId) {
        return ResponseEntity.ofNullable(messageService.getMessagesForResponse(responseId));
    }

    @GetMapping("unread/by-user")
    public ResponseEntity<Integer> getUnreadMessages(@RequestParam Long userId) {
        return ResponseEntity.ofNullable(messageService.getUnreadMessagesCount(userId));
    }

    @GetMapping("unread/by-vacancy")
    public ResponseEntity<Integer> getUnreadMessagesByVacancy(@RequestParam Long vacancyId,
                                                              @RequestParam Long userId) {
        return ResponseEntity.ofNullable(messageService.getUnreadMessagesCountByVacancy(vacancyId, userId));
    }

    @GetMapping("unread/by-resume")
    public ResponseEntity<Integer> getUnreadMessagesByResume(@RequestParam Long resumeId,
                                                             @RequestParam Long userId) {
        return ResponseEntity.ofNullable(messageService.getUnreadMessagesCountByResume(resumeId, userId));
    }

    @GetMapping("unread/by-response")
    public ResponseEntity<Integer> getUnreadMessagesByResponse(@RequestParam Long responseId,
                                                               @RequestParam Long userId) {
        return ResponseEntity.ofNullable(messageService.getUnreadMessagesCountByResponse(responseId, userId));
    }

    @PostMapping("/{messageId}/read")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Long messageId) {
        messageService.markMessageAsRead(messageId);
        return ResponseEntity.ok().build();
    }
}
