package kg.attractor.job_search.controller.mvc;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.interfaces.ResumeService;
import kg.attractor.job_search.service.interfaces.UserService;
import kg.attractor.job_search.service.interfaces.VacancyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller("mvcEmployer")
@RequestMapping("employers")
public class EmployerController extends UserController {
    public EmployerController(UserService userService, VacancyService vacancyService,  ResumeService resumeService) {
        super(userService, vacancyService, resumeService);
    }

    @GetMapping
    @PreAuthorize("hasRole('APPLICANT')")
    public String employers(Model model) {
        model.addAttribute("url", getUrl());
        return "profile/users";
    }

    @Override
    @GetMapping("{userId}")
    @PreAuthorize("hasRole('APPLICANT') or (hasRole('EMPLOYER') and @userService.isCurrentUser(#userId))")
    public String profile(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            Model model) {

        return super.profile(userId, page, size, model);
    }

    @Override
    @GetMapping("{userId}/edit")
    @PreAuthorize("hasRole('EMPLOYER') and @userService.isCurrentUser(#userId)")
    public String editProfile(@PathVariable Long userId, Model model) {
        return super.editProfile(userId, model);
    }

    @Override
    @PostMapping("{userId}/avatar")
    @PreAuthorize("hasRole('EMPLOYER') and @userService.isCurrentUser(#userId)")
    public String uploadAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        return super.uploadAvatar(userId, file);
    }

    @Override
    @PostMapping("{userId}/edit")
    @PreAuthorize("hasRole('EMPLOYER') and @userService.isCurrentUser(#userId)")
    public String editProfile(@PathVariable Long userId,
                              @ModelAttribute("userDto") @Valid UserDto userDto,
                              BindingResult bindingResult,
                              Model model) {
        return super.editProfile(userId, userDto, bindingResult, model);
    }

    @Override
    protected UserDto getUserById(Long userId) {
        return userService.getEmployerById(userId);
    }
}
