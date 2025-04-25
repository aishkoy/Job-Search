package kg.attractor.job_search.controller;

import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("mvcMain")
@RequestMapping
@RequiredArgsConstructor
public class MainController {
    private final VacancyService vacancyService;
    private final ResumeService resumeService;

    @GetMapping
    public String getMainPage(Model model) {
        model.addAttribute("vacancies", vacancyService.getLastVacancies());
        model.addAttribute("resumes", resumeService.getLastResumes());
        return "index";
    }

}
