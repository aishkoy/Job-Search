package kg.attractor.job_search.controller;

import kg.attractor.job_search.service.UserService;
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
    private final UserService userService;

    @GetMapping
    public String getMainPage(Model model) {
        model.addAttribute("vacancies", vacancyService.getLast3Vacancies());
        return "index";
    }

    @GetMapping("employers")
    public String getEmployers(Model model) {
        model.addAttribute("users", userService.getEmployers());
        return "profile/users";
    }

    @GetMapping("applicants")
    public String getApplicants(Model model) {
        model.addAttribute("users", userService.getApplicants());
        return "profile/users";
    }

}
