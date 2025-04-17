package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import kg.attractor.job_search.dto.vacancy.VacancyFormDto;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{id}")
    public String vacancy(@PathVariable Long id, Model model) {
        UserDto userDto = null;
        try {
            userDto = userService.getAuthUser();
        } catch (Exception ignored) {}
        model.addAttribute("currentUser", userDto);
        model.addAttribute("vacancy", vacancyService.getVacancyById(id));
        return "vacancy/vacancy";
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
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.getAuthUser());
            return "vacancy/create-vacancy";
        }

        vacancyService.createVacancy(vacancyFormDto);
        return "redirect:/profile";
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable("id") Long vacancyId, Model model) {
        VacancyDto dto = vacancyService.getVacancyDtoByIdAndAuthor(vacancyId, userService.getAuthId());
        VacancyFormDto formDto = vacancyService.convertToFormDto(dto);
        model.addAttribute("vacancy", dto);
        model.addAttribute("vacancyForm", formDto);
        return "vacancy/edit-vacancy";
    }

    @PostMapping("{id}/edit")
    public String edit(@PathVariable("id") Long vacancyId,
                       @ModelAttribute("vacancyForm") @Valid VacancyFormDto vacancyDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            VacancyDto vacancy = vacancyService.getVacancyDtoByIdAndAuthor(vacancyId, userService.getAuthId());
            model.addAttribute("vacancy", vacancy);
            return "vacancy/edit-vacancy";
        }
        vacancyService.updateVacancy(vacancyId, vacancyDto);
        return "redirect:/profile";
    }
}
