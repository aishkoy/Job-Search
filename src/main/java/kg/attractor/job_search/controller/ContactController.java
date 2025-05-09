package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.service.ContactInfoService;
import kg.attractor.job_search.service.ContactTypeService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller("mvcContacts")
@RequestMapping("/resumes/{resumeId}/contacts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('APPLICANT') and @resumeService.isAuthorOfResume(#resumeId, authentication.principal.userId)")
public class ContactController {
    private final ContactInfoService contactInfoService;
    private final ContactTypeService contactTypeService;
    private final UserService userService;
    private final ResumeService resumeService;

    @GetMapping("/create")
    public String showCreateContactModal(@PathVariable Long resumeId,
                                         Model model) {
        resumeService.getResumeById(resumeId, userService.getAuthId());

        ContactInfoDto contactDto = new ContactInfoDto();
        contactDto.setResumeId(resumeId);

        model.addAttribute("mode", "create");
        model.addAttribute("contactDto", contactDto);
        model.addAttribute("contactTypes", contactTypeService.getAllContactTypes());
        model.addAttribute("action", "/resumes/" + resumeId + "/contacts");

        return "info/contact";
    }

    @PostMapping
    public String createContact(@PathVariable Long resumeId,
                                @ModelAttribute("contactDto") @Valid ContactInfoDto contactDto,
                                BindingResult bindingResult,
                                Model model) {

        resumeService.getResumeById(resumeId, userService.getAuthId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            model.addAttribute("contactTypes", contactTypeService.getAllContactTypes());
            model.addAttribute("action", "/resumes/" + resumeId + "/contacts");
            return "info/contact";
        }

        contactInfoService.createContactInfo(contactDto);

        return "redirect:/resumes/" + resumeId + "/edit";
    }

    @GetMapping("/{contactId}/edit")
    public String showEditContactModal(@PathVariable Long resumeId,
                                       @PathVariable Long contactId,
                                       Model model) {
        resumeService.getResumeById(resumeId, userService.getAuthId());

        ContactInfoDto contactDto = contactInfoService.getContactInfoById(contactId);

        model.addAttribute("mode", "edit");
        model.addAttribute("contactDto", contactDto);
        model.addAttribute("contactTypes", contactTypeService.getAllContactTypes());
        model.addAttribute("action", "/resumes/" + resumeId + "/contacts/" + contactId + "/edit");

        return "info/contact";
    }

    @PostMapping("/{contactId}/edit")
    public String editContact(@PathVariable Long resumeId,
                              @PathVariable Long contactId,
                              @ModelAttribute("contactDto") @Valid ContactInfoDto contactDto,
                              BindingResult bindingResult,
                              Model model) {
        resumeService.getResumeById(resumeId, userService.getAuthId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("contactTypes", contactTypeService.getAllContactTypes());
            model.addAttribute("action", "/resumes/" + resumeId + "/contacts/" + contactId + "/edit");
            return "info/contact";
        }

        contactInfoService.updateContactInfo(contactDto);

        return "redirect:/resumes/" + resumeId + "/edit";
    }

    @PostMapping("/{contactId}/delete")
    public String deleteContact(@PathVariable Long resumeId,
                                @PathVariable Long contactId) {

        resumeService.getResumeById(resumeId, userService.getAuthId());
        contactInfoService.deleteContactInfoById(contactId);
        return "redirect:/resumes/" + resumeId + "/edit";
    }
}