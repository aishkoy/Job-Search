package kg.attractor.job_search.controller.mvc;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.service.interfaces.EducationInfoService;
import kg.attractor.job_search.service.interfaces.ResumeService;
import kg.attractor.job_search.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller("mvcEducation")
@RequestMapping("/resumes/{resumeId}/educations")
@RequiredArgsConstructor
@PreAuthorize("hasRole('APPLICANT') and @resumeService.isAuthorOfResume(#resumeId, authentication.principal.userId)")
public class EducationController {
    private final EducationInfoService educationInfoService;
    private final UserService userService;
    private final ResumeService resumeService;

    @GetMapping("/create")
    public String showCreateEducationModal(@PathVariable Long resumeId,
                                           Model model) {
        resumeService.getResumeById(resumeId, userService.getAuthId());

        EducationInfoDto educationDto = new EducationInfoDto();
        educationDto.setResumeId(resumeId);

        model.addAttribute("mode", "create");
        model.addAttribute("educationDto", educationDto);
        model.addAttribute("action", "/resumes/" + resumeId + "/educations/create");

        return "info/education";
    }

    @PostMapping("create")
    public String createEducation(@PathVariable Long resumeId,
                                  @ModelAttribute("educationDto") @Valid EducationInfoDto educationDto,
                                  BindingResult bindingResult,
                                  Model model) {
        resumeService.getResumeById(resumeId, userService.getAuthId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            model.addAttribute("action", "/resumes/" + resumeId + "/educations/create");
            return "info/education";
        }

        educationInfoService.createEducationInfo(educationDto);

        return "redirect:/resumes/" + resumeId + "/edit";
    }

    @GetMapping("/{educationId}/edit")
    public String showEditEducationModal(@PathVariable Long resumeId,
                                         @PathVariable Long educationId,
                                         Model model) {
        resumeService.getResumeById(resumeId, userService.getAuthId());

        EducationInfoDto educationDto = educationInfoService.getEducationInfoById(educationId);
        educationDto.setResumeId(resumeId);

        model.addAttribute("mode", "edit");
        model.addAttribute("educationDto", educationDto);
        model.addAttribute("action", "/resumes/" + resumeId + "/educations/" + educationId + "/edit");

        return "info/education";
    }

    @PostMapping("/{educationId}/edit")
    public String editEducation(@PathVariable Long resumeId,
                                @PathVariable Long educationId,
                                @ModelAttribute("educationDto") @Valid EducationInfoDto educationDto,
                                BindingResult bindingResult,
                                Model model) {
        resumeService.getResumeById(resumeId, userService.getAuthId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("action", "/resumes/" + resumeId + "/educations/" + educationId + "/edit");
            return "info/education";
        }

        educationInfoService.updateEducationInfo(educationDto);

        return "redirect:/resumes/" + resumeId + "/edit";
    }

    @PostMapping("/{educationId}/delete")
    public String deleteEducation(@PathVariable Long resumeId,
                                  @PathVariable Long educationId) {
        resumeService.getResumeById(resumeId, userService.getAuthId());
        educationInfoService.deleteEducationInfo(educationId);

        return "redirect:/resumes/" + resumeId + "/edit";
    }
}