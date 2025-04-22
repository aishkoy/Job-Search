package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.dto.resume.ResumeFormDto;
import kg.attractor.job_search.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping("{id}")
    public String getResume(@PathVariable("id") Long resumeId, Model model) {
        model.addAttribute("currentUser", userService.getAuthUser());
        model.addAttribute("resume", resumeService.getResumeDtoById(resumeId, userService.getAuthId()));
        return "resume/resume";
    }

    @GetMapping("create")
    public String createResume(Model model) {
        ResumeFormDto resumeFormDto = new ResumeFormDto();
        resumeFormDto.setApplicant(userService.getAuthUser());
        model.addAttribute("resumeForm", resumeFormDto);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("contactTypes", contactTypeService.getAllContactTypes());

        return "resume/create-resume";
    }

    @PostMapping("/create")
    public String createResume(@ModelAttribute("resumeForm") @Valid ResumeFormDto resumeForm,
                               BindingResult bindingResult,
                               @RequestParam(required = false) String action,
                               Model model) {

        switch (action) {
            case "addExperience" -> resumeService.addExperience(resumeForm);
            case "addEducation" -> resumeService.addEducation(resumeForm);
            case "addContact" -> resumeService.addContact(resumeForm);
            case "save" -> {
                if (bindingResult.hasErrors()) {
                    break;
                }
                resumeService.createResume(resumeForm);
                return "redirect:/profile";
            }
        }

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("contactTypes", contactTypeService.getAllContactTypes());
        return "resume/create-resume";
    }

    @GetMapping("{id}/edit")
    public String editResume(@PathVariable("id") Long resumeId, Model model) {
        ResumeDto resume = resumeService.getResumeDtoById(resumeId, userService.getAuthId());
        ResumeFormDto formDto = resumeService.convertToFormDto(resume);

        model.addAttribute("currentUser", userService.getAuthUser());
        model.addAttribute("resume", resume);
        model.addAttribute("resumeForm", formDto);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("contactTypes", contactTypeService.getAllContactTypes());
        model.addAttribute("contactInfo", new ContactInfoDto());

        return "resume/edit-resume";
    }

    @GetMapping("{id}/delete")
    public String deleteResume(@PathVariable("id") Long resumeId) {
        resumeService.deleteResume(resumeId, userService.getAuthId());
        return "redirect:/";
    }

    @PostMapping("{id}/edit")
    public String updateResume(@ModelAttribute("resumeForm") @Valid ResumeFormDto resumeForm,
                               BindingResult bindingResult,
                               Model model,
                               @PathVariable("id") Long resumeId) {


        model.addAttribute("currentUser", userService.getAuthUser());
        model.addAttribute("resumeForm", resumeForm);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("contactTypes", contactTypeService.getAllContactTypes());
        model.addAttribute("resume", resumeService.getResumeById(resumeId, userService.getAuthId()));

        if (bindingResult.hasErrors()) {
            return "resume/edit-resume";
        }

        resumeService.updateResume(resumeId, resumeForm);
        return "redirect:/profile";
    }

}
