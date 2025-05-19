package kg.attractor.job_search.controller.mvc;

import kg.attractor.job_search.dto.MessageDto;
import kg.attractor.job_search.dto.ResponseDto;
import kg.attractor.job_search.service.interfaces.MessageService;
import kg.attractor.job_search.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final UserService userService;

    @MessageMapping("/chat.send.{responseId}")
    @SendTo("/topic/response/{responseId}")
    public MessageDto sendMessage(@DestinationVariable Long responseId, MessageDto message) {
        message.setResponse(ResponseDto.builder().id(responseId).build());
        return messageService.saveMessage(message);
    }

    @GetMapping("/chat")
    public String chatMainPage(Model model) {
        var currentUser = userService.getAuthUser();
        boolean isEmployer = currentUser.getRole().getName().equals("EMPLOYER");

        model.addAttribute("isEmployer", isEmployer);

        return "chat/main";
    }

    @PreAuthorize("hasRole('EMPLOYER') and @vacancyService.isAuthorOfVacancy(#vacancyId, authentication.principal.userId)")
    @GetMapping("/chat/vacancy/{vacancyId}")
    public String vacancyResponsesPage(@PathVariable("vacancyId") Long vacancyId, Model model) {
        model.addAttribute("vacancyId", vacancyId);
        return "chat/responses";
    }

    @PreAuthorize("hasRole('APPLICANT') and @resumeService.isAuthorOfResume(#resumeId, authentication.principal.userId)")
    @GetMapping("/chat/resume/{resumeId}")
    public String resumeResponsesPage(@PathVariable("resumeId") Long resumeId, Model model) {
        model.addAttribute("resumeId", resumeId);
        return "chat/responses";
    }

    @PreAuthorize("@responseService.isChatParticipant(#responseId, authentication.principal.userId)")
    @GetMapping("/chat/room/{responseId}")
    public String chatRoom(@PathVariable("responseId") Long responseId, Model model) {
        model.addAttribute("responseId", responseId);
        return "chat/room";
    }
}
