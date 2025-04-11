package kg.attractor.job_search.controller;

import kg.attractor.job_search.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("mvcResumes")
@RequestMapping("resumes")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @GetMapping
    public String getResumes(Model model) {
        model.addAttribute("resumes", resumeService.getResumes());
        return "resume/resumes";
    }
}
