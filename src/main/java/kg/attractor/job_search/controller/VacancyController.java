package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.vacancy.VacancyFormDto;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("mvcVacancies")
@RequestMapping("vacancies")
@RequiredArgsConstructor
public class VacancyController {
    private final VacancyService vacancyService;
    private final UserService userService;

    @GetMapping
    public String vacancies(Model model) {
        model.addAttribute("vacancies", vacancyService.getVacancies());
        return "vacancy/vacancies";
    }

    @GetMapping("create")
    public String create(Model model) {
        model.addAttribute("user", userService.getAuthUser());
        model.addAttribute("vacancyForm", new VacancyFormDto());
        return "vacancy/create-vacancy";
    }

    @PostMapping("create")
    public String create(@ModelAttribute("vacancyForm") @Valid VacancyFormDto vacancyFormDto,
                         BindingResult bindingResult,
                         Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("user", userService.getAuthUser());
            return "vacancy/create-vacancy";
        }

        vacancyService.createVacancy(vacancyFormDto);
        return "redirect:/profile";
    }
}
