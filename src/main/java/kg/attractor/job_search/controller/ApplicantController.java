package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("mvcApplicant")
@RequestMapping("applicants")
public class ApplicantController extends UserController {

    public ApplicantController(UserService userService) {
        super(userService);
    }

    @GetMapping
    public String getApplicants(Model model) {
        model.addAttribute("url", getUrl());
        model.addAttribute("users", userService.getApplicants());
        return "profile/users";
    }

    @Override
    protected UserDto getUserById(Long userId) {
        return userService.getApplicantById(userId);
    }
}
