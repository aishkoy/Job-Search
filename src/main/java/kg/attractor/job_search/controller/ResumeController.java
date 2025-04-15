package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.dto.resume.ResumeFormDto;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
        model.addAttribute("resume",resumeService.getResumeById(resumeId, userService.getAuthId()));
        return "resume/resume";
    }

    @GetMapping("create")
    public String createResume(Model model) {
        ResumeFormDto resumeFormDto = new ResumeFormDto();
        resumeFormDto.setApplicantId(userService.getAuthId());
        model.addAttribute("resumeForm", resumeFormDto);

        return "resume/create-resume";
    }

    @PostMapping("/create")
    public String createResume(@ModelAttribute("resumeForm") @Valid ResumeFormDto resumeForm,
                               BindingResult bindingResult,
                               @RequestParam(required = false) String action,
                               Model model) {

        switch (action) {
            case "addExperience" -> addExperience(resumeForm);
            case "addEducation" -> addEducation(resumeForm);
            case "addContact" -> addContact(resumeForm);
            default -> {
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

    private void addExperience(ResumeFormDto resumeForm) {
        if (resumeForm.getWorkExperiences() == null) {
            resumeForm.setWorkExperiences(new ArrayList<>());
        }
        resumeForm.getWorkExperiences().add(new WorkExperienceInfoDto());
    }

    private void addEducation(ResumeFormDto resumeForm) {
        if (resumeForm.getEducations() == null) {
            resumeForm.setEducations(new ArrayList<>());
        }
        resumeForm.getEducations().add(new EducationInfoDto());
    }

    private void addContact(ResumeFormDto resumeForm) {
        if (resumeForm.getContacts() == null) {
            resumeForm.setContacts(new ArrayList<>());
        }
        resumeForm.getContacts().add(new ContactInfoDto());
    }
}
