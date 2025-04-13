package kg.attractor.job_search.controller;

import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("mvcVacancies")
@RequestMapping("vacancies")
@RequiredArgsConstructor
public class VacancyController {
    private final VacancyService vacancyService;

    @GetMapping
    public String vacancies(Model model) {
        model.addAttribute("vacancies", vacancyService.getVacancies());
        return "vacancy/vacancies";
    }
}
