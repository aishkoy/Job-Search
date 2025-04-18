package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller("mvcAuth")
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("userDto", new CreateUserDto());
        return "auth/register";
    }

    @PostMapping("register")
    public String login(@ModelAttribute("userDto") @Valid CreateUserDto userDto,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        userService.registerUser(userDto);
        return "redirect:/";
    }
    @GetMapping("login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if(error != null) {
            model.addAttribute("error", "Неверный логин или пароль");
        }
        return "auth/login";
    }
}
