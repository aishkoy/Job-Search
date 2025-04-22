package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("mvcEmployer")
@RequestMapping("employers")
public class EmployerController extends UserController {
    public EmployerController(UserService userService) {
        super(userService);
    }

    @GetMapping
    public String employers(Model model) {
        model.addAttribute("url", getUrl());
        model.addAttribute("users", userService.getEmployers());
        return "profile/users";
    }

    @Override
    protected UserDto getUserById(Long userId) {
        return userService.getEmployerById(userId);
    }
}
