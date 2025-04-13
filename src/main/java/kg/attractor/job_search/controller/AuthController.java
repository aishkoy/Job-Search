package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller("mvcAuth")
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("register")
    public String login(@ModelAttribute @Valid CreateUserDto userDto) {
        userService.registerUser(userDto);
        return "redirect:/auth/login";
    }

    @GetMapping("login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        return "redirect:/profile";
    }

}
