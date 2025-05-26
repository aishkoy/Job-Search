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
}
