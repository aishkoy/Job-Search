package kg.attractor.job_search.controller.mvc;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.interfaces.ResumeService;
import kg.attractor.job_search.service.interfaces.UserService;
import kg.attractor.job_search.service.interfaces.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public abstract class UserController {
    protected final UserService userService;
    protected final VacancyService vacancyService;
    protected final ResumeService resumeService;

    protected abstract UserDto getUserById(Long userId);

    protected String getUrl() {
        RequestMapping mapping = this.getClass().getAnnotation(RequestMapping.class);
        return mapping != null && mapping.value().length > 0
                ? mapping.value()[0].replace("/", "")
                : "";
    }

    @GetMapping("{userId}")
    public String profile(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            Model model) {

        UserDto user = getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("size", size);
        model.addAttribute("url", getUrl());

        Page<VacancyDto> vacancies = null;
        Page<ResumeDto> resumes = null;

        try {
            vacancies = vacancyService.getVacanciesByEmployerId(user.getId(), page, size);
        } catch (NoSuchElementException ignore) {
        }
        try {
            resumes = resumeService.getResumesByApplicantId(user.getId(), page, size);
        } catch (NoSuchElementException ignore) {
        }


        model.addAttribute("vacanciesPage", vacancies);
        model.addAttribute("resumesPage", resumes);

        return "profile/profile";
    }

    @GetMapping("{userId}/edit")
    public String editProfile(@PathVariable Long userId,
                              Model model) {
        UserDto user = getUserById(userId);
        model.addAttribute("url", getUrl());
        model.addAttribute("userDto", user);
        return "profile/edit-profile";
    }

    @PostMapping("{userId}/avatar")
    public String uploadAvatar(@PathVariable Long userId,
                               @RequestParam("file") MultipartFile file) {
        userService.uploadAvatar(file, userId);
        return "redirect:/%s/%d".formatted(getUrl(), userId);
    }

    @PostMapping("{userId}/edit")
    public String editProfile(@PathVariable Long userId,
                              @ModelAttribute("userDto") @Valid UserDto userDto,
                              BindingResult bindingResult,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("url", getUrl());
            return "profile/edit-profile";
        }

        userService.updateUser(userId, userDto);
        return "redirect:/%s/%d".formatted(getUrl(), userId);
    }

}
