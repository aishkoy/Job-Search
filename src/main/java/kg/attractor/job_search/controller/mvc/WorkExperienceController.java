package kg.attractor.job_search.controller.mvc;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.service.interfaces.WorkExperienceInfoService;
import kg.attractor.job_search.service.interfaces.ResumeService;
import kg.attractor.job_search.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller("mvcWorkExperience")
@RequestMapping("/resumes/{resumeId}/experiences")
@PreAuthorize("hasRole('APPLICANT') and @resumeService.isAuthorOfResume(#resumeId, authentication.principal.userId)")
@RequiredArgsConstructor
public class WorkExperienceController {
    private final WorkExperienceInfoService workExperienceInfoService;
    private final UserService userService;
    private final ResumeService resumeService;

    @GetMapping("/create")
    public String showCreateExperienceModal(@PathVariable("resumeId") Long resumeId,
                                            Model model
    ) {
        resumeService.getResumeById(resumeId, userService.getAuthId());

        WorkExperienceInfoDto experienceDto = new WorkExperienceInfoDto();
        experienceDto.setResumeId(resumeId);

        model.addAttribute("mode", "create");
        model.addAttribute("experienceDto", experienceDto);
        model.addAttribute("action", "/resumes/" + resumeId + "/experiences/create");

        return "info/experience";
    }

    @PostMapping("/create")
    public String createExperience(@PathVariable("resumeId") Long resumeId,
                                   @ModelAttribute("experienceDto") @Valid WorkExperienceInfoDto experienceDto,
                                   BindingResult bindingResult,
                                   Model model
    ) {
        resumeService.getResumeById(resumeId, userService.getAuthId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            model.addAttribute("action", "/resumes/" + resumeId + "/experiences/create");
            return "info/experience";
        }

        workExperienceInfoService.createWorkExperience(experienceDto);

        return "redirect:/resumes/" + resumeId + "/edit";
    }

    @GetMapping("/{experienceId}/edit")
    public String showEditExperienceModal(@PathVariable("resumeId") Long resumeId,
                                          @PathVariable("experienceId") Long experienceId,
                                          Model model
    ) {
        resumeService.getResumeById(resumeId, userService.getAuthId());

        WorkExperienceInfoDto experienceDto = workExperienceInfoService.getWorkExperienceById(experienceId);
        experienceDto.setResumeId(resumeId);

        model.addAttribute("mode", "edit");
        model.addAttribute("experienceDto", experienceDto);
        model.addAttribute("action", "/resumes/" + resumeId + "/experiences/" + experienceId + "/edit");

        return "info/experience";
    }

    @PostMapping("/{experienceId}/edit")
    public String editExperience(@PathVariable("resumeId") Long resumeId,
                                 @PathVariable("experienceId") Long experienceId,
                                 @ModelAttribute("experienceDto") @Valid WorkExperienceInfoDto experienceDto,
                                 BindingResult bindingResult,
                                 Model model
    ) {
        resumeService.getResumeById(resumeId, userService.getAuthId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("action", "/resumes/" + resumeId + "/experiences/" + experienceId + "/edit");
            return "info/experience";
        }

        workExperienceInfoService.updateWorkExperienceInfo(experienceDto);

        return "redirect:/resumes/" + resumeId + "/edit";
    }

    @PostMapping("/{experienceId}/delete")
    public String deleteExperience(@PathVariable("resumeId") Long resumeId,
                                   @PathVariable("experienceId") Long experienceId
    ) {
        resumeService.getResumeById(resumeId, userService.getAuthId());

        workExperienceInfoService.deleteWorkExperienceInfo(experienceId);

        return "redirect:/resumes/" + resumeId + "/edit";
    }
}