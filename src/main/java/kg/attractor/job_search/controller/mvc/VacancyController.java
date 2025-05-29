package kg.attractor.job_search.controller.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.interfaces.CategoryService;
import kg.attractor.job_search.service.interfaces.UserService;
import kg.attractor.job_search.service.interfaces.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public String vacancies(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "vacancy/vacancies";
    }

    @GetMapping("{vacancyId}")
    public String vacancy(@PathVariable Long vacancyId, Model model) {
        UserDto userDto = null;
        try {
            userDto = userService.getAuthUser();
        } catch (Exception ignored) {
        }
        model.addAttribute("currentUser", userDto);
        model.addAttribute("vacancy", vacancyService.getVacancyById(vacancyId));
        return "vacancy/vacancy";
    }

    @GetMapping("create")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String create(Model model) {
        model.addAttribute("user", userService.getAuthUser());
        model.addAttribute("vacancyForm", new VacancyDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "vacancy/create-vacancy";
    }

    @PostMapping("create")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String create(@ModelAttribute("vacancyForm") @Valid VacancyDto vacancyFormDto,
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

    @GetMapping("{vacancyId}/edit")
    @PreAuthorize("hasRole('EMPLOYER') and @vacancyService.isAuthorOfVacancy(#vacancyId, authentication.principal.userId)")
    public String edit(@PathVariable("vacancyId") Long vacancyId, Model model) {
        VacancyDto dto = vacancyService.getVacancyDtoByIdAndAuthor(vacancyId, userService.getAuthId());
        model.addAttribute("vacancyForm", dto);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "vacancy/edit-vacancy";
    }

    @PostMapping("{vacancyId}/delete")
    @PreAuthorize("hasRole('EMPLOYER') and @vacancyService.isAuthorOfVacancy(#vacancyId, authentication.principal.userId)")
    public String delete(@PathVariable("vacancyId") Long vacancyId) {
        vacancyService.deleteVacancy(vacancyId, userService.getAuthId());
        return "redirect:/employers/" + userService.getAuthId();
    }

    @PostMapping("{vacancyId}/edit")
    @PreAuthorize("hasRole('EMPLOYER') and @vacancyService.isAuthorOfVacancy(#vacancyId, authentication.principal.userId)")
    public String edit(@PathVariable("vacancyId") Long vacancyId,
                       @ModelAttribute("vacancyForm") @Valid VacancyDto vacancyDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "vacancy/edit-vacancy";
        }
        vacancyService.updateVacancy(vacancyId, vacancyDto);
        return "redirect:/vacancies/" + vacancyId;
    }

    @PostMapping("{vacancyId}/update")
    @PreAuthorize("hasRole('EMPLOYER') and @vacancyService.isAuthorOfVacancy(#vacancyId, authentication.principal.userId)")
    public String updateVacancyStatus(@PathVariable("vacancyId") Long vacancyId,
                                      HttpServletRequest request) {
        vacancyService.updateVacancy(vacancyId, userService.getAuthId());

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
