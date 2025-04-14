package kg.attractor.job_search.controller;

import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
