package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.dto.vacancy.VacancyDto;
import kg.attractor.job_search.dto.vacancy.VacancyFormDto;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller("mvcVacancies")
@RequestMapping("vacancies")
@RequiredArgsConstructor
public class VacancyController {
    private final VacancyService vacancyService;
    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping
    public String vacancies(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(required = false) Long categoryId,
                            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
                            @RequestParam(required = false, defaultValue = "desc") String sortDirection,
                            Model model) {

        Page<VacancyDto> vacanciesPage = vacancyService.getActiveVacanciesPage(
                page, size, categoryId, sortBy, sortDirection
        );

        model.addAttribute("size", size);
        model.addAttribute("vacancies", vacanciesPage);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);

        return "vacancy/vacancies";
    }

    @GetMapping("{id}")
    public String vacancy(@PathVariable Long id, Model model) {
        UserDto userDto = null;
        try {
            userDto = userService.getAuthUser();
        } catch (Exception ignored) {
        }
        model.addAttribute("currentUser", userDto);
        model.addAttribute("vacancy", vacancyService.getVacancyById(id));
        return "vacancy/vacancy";
    }

    @GetMapping("create")
    public String create(Model model) {
        model.addAttribute("user", userService.getAuthUser());
        model.addAttribute("vacancyForm", new VacancyFormDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "vacancy/create-vacancy";
    }

    @PostMapping("create")
    public String create(@ModelAttribute("vacancyForm") @Valid VacancyFormDto vacancyFormDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.getAuthUser());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "vacancy/create-vacancy";
        }

        Long vacancyId = vacancyService.createVacancy(vacancyFormDto);
        return "redirect:/vacancies/" + vacancyId;
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable("id") Long vacancyId, Model model) {
        VacancyDto dto = vacancyService.getVacancyDtoByIdAndAuthor(vacancyId, userService.getAuthId());
        VacancyFormDto formDto = vacancyService.convertToFormDto(dto);
        model.addAttribute("vacancy", dto);
        model.addAttribute("vacancyForm", formDto);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "vacancy/edit-vacancy";
    }

    @GetMapping("{id}/delete")
    public String delete(@PathVariable("id") Long vacancyId) {
        vacancyService.deleteVacancy(vacancyId, userService.getAuthId());
        return "redirect:/";
    }

    @PostMapping("{id}/edit")
    public String edit(@PathVariable("id") Long vacancyId,
                       @ModelAttribute("vacancyForm") @Valid VacancyFormDto vacancyDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            VacancyDto vacancy = vacancyService.getVacancyDtoByIdAndAuthor(vacancyId, userService.getAuthId());
            model.addAttribute("vacancy", vacancy);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "vacancy/edit-vacancy";
        }
        vacancyService.updateVacancy(vacancyId, vacancyDto);
        return "redirect:/vacancies/" + vacancyId;
    }
}
