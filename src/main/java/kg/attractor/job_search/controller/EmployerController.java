package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("mvcEmployer")
@RequestMapping("employers")
public class EmployerController extends UserController {
    public EmployerController(UserService userService) {
        super(userService);
    }

    @GetMapping
    public String employers(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "5") int size,
                            Model model) {
        model.addAttribute("url", getUrl());
        model.addAttribute("size", size);
        model.addAttribute("users", userService.getEmployersPage(page, size));
        return "profile/users";
    }

    @Override
    protected UserDto getUserById(Long userId) {
        return userService.getEmployerById(userId);
    }
}
