package kg.attractor.job_search.controller.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.service.interfaces.CategoryService;
import kg.attractor.job_search.service.interfaces.ContactTypeService;
import kg.attractor.job_search.service.interfaces.ResumeService;
import kg.attractor.job_search.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller("mvcResumes")
@RequestMapping("resumes")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ContactTypeService contactTypeService;

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public String getResumes(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(required = false) Long categoryId,
                             Model model) {
        Page<ResumeDto> resumePage;

        if (categoryId != null) {
            resumePage = resumeService.getResumesPageByCategoryId(page, size, categoryId);
            model.addAttribute("categoryId", categoryId);
        } else {
            resumePage = resumeService.getActiveResumesPage(page, size);
        }

        model.addAttribute("resumes", resumePage);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "resume/resumes";
    }

    @GetMapping("{resumeId}")
    @PreAuthorize("hasRole('EMPLOYER') or (hasRole('APPLICANT') and @resumeService.isAuthorOfResume(#resumeId, authentication.principal.userId))")
    public String getResume(@PathVariable("resumeId") Long resumeId, Model model) {
        model.addAttribute("currentUser", userService.getAuthUser());
        model.addAttribute("resume", resumeService.getResumeDtoById(resumeId, userService.getAuthId()));
        return "resume/resume";
    }

    @GetMapping("create")
    @PreAuthorize("hasRole('APPLICANT')")
    public String createResume(Model model) {
        ResumeDto resumeDto = new ResumeDto();
        resumeDto.setApplicant(userService.getAuthUser());
        model.addAttribute("resumeForm", resumeDto);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("contactTypes", contactTypeService.getAllContactTypes());

        return "resume/create-resume";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('APPLICANT')")
    public String createResume(@ModelAttribute("resumeForm") @Valid ResumeDto resumeForm,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("contactTypes", contactTypeService.getAllContactTypes());
            return "resume/create-resume";
        }
        Long resumeId = resumeService.createResume(resumeForm);
        return "redirect:/resumes/" + resumeId;
    }

    @GetMapping("{resumeId}/edit")
    @PreAuthorize("hasRole('APPLICANT') and @resumeService.isAuthorOfResume(#resumeId, authentication.principal.userId)")
    public String editResume(@PathVariable("resumeId") Long resumeId, Model model) {
        ResumeDto resume = resumeService.getResumeDtoById(resumeId, userService.getAuthId());

        model.addAttribute("currentUser", userService.getAuthUser());
        model.addAttribute("resume", resume);
        model.addAttribute("resumeForm", resume);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("contactTypes", contactTypeService.getAllContactTypes());

        return "resume/edit-resume";
    }

    @PostMapping("{resumeId}/delete")
    @PreAuthorize("hasRole('APPLICANT') and @resumeService.isAuthorOfResume(#resumeId, authentication.principal.userId)")
    public String deleteResume(@PathVariable("resumeId") Long resumeId) {
        resumeService.deleteResume(resumeId, userService.getAuthId());
        return "redirect:/applicants/" + userService.getAuthId();
    }

    @PostMapping("{resumeId}/edit")
    @PreAuthorize("hasRole('APPLICANT') and @resumeService.isAuthorOfResume(#resumeId, authentication.principal.userId)")
    public String updateResume(@ModelAttribute("resumeForm") @Valid ResumeDto resumeForm,
                               BindingResult bindingResult,
                               Model model,
                               @PathVariable("resumeId") Long resumeId) {

        model.addAttribute("currentUser", userService.getAuthUser());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("contactTypes", contactTypeService.getAllContactTypes());
        model.addAttribute("resume", resumeService.getResumeById(resumeId, userService.getAuthId()));

        if (bindingResult.hasErrors()) {
            return "resume/edit-resume";
        }

        resumeService.updateResume(resumeId, resumeForm);
        return "redirect:/resumes/" + resumeId;
    }

    @PostMapping("{resumeId}/update")
    @PreAuthorize("hasRole('APPLICANT') and @resumeService.isAuthorOfResume(#resumeId, authentication.principal.userId)")
    public String updateResumeStatus(@PathVariable("resumeId") Long resumeId,
                                     HttpServletRequest request) {
        resumeService.updateResume(resumeId, userService.getAuthId());

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
