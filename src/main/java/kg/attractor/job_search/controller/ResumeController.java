package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.resume.ResumeDto;
import kg.attractor.job_search.dto.resume.ResumeFormDto;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public String getResumes(Model model) {
        model.addAttribute("resumes", resumeService.getResumes());
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
                    return "resume/create-resume";
                }
                resumeService.createResume(resumeForm);
                return "redirect:/profile";
            }
        }

        model.addAttribute("resumeForm", resumeForm);
        return "resume/create-resume";
    }

    @GetMapping("{id}/edit")
    public String editResume(@PathVariable("id") Long resumeId, Model model) {
        ResumeDto resume = resumeService.getResumeDtoById(resumeId, userService.getAuthId());
        ResumeFormDto formDto = resumeService.convertToFormDto(resume);

        model.addAttribute("currentUser", userService.getAuthUser());
        model.addAttribute("resume", resume);
        model.addAttribute("resumeForm", formDto);
        return "resume/edit-resume";
    }

    @PostMapping("{id}/edit")
    public String updateResume(@ModelAttribute("resumeForm") @Valid ResumeFormDto resumeForm,
                               BindingResult bindingResult,
                               @RequestParam(required = false) String action,
                               Model model,
                               @PathVariable("id") Long resumeId) {


        model.addAttribute("currentUser", userService.getAuthUser());
        model.addAttribute("resumeForm", resumeForm);
        model.addAttribute("resume", resumeService.getResumeById(resumeId, userService.getAuthId()));

        if (bindingResult.hasErrors()) {
            return "resume/edit-resume";
        }

        resumeService.updateResume(resumeId, resumeForm);
        return "redirect:/profile";
    }
}
