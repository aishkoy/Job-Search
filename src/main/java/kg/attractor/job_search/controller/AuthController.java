package kg.attractor.job_search.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.exception.InvalidPasswordException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.service.RoleService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Locale;

@Controller("mvcAuth")
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final RoleService roleService;
    private final MessageSource messageSource;

    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("userDto", new CreateUserDto());
        model.addAttribute("roles", roleService.getAllRoles());
        return "auth/register";
    }

    @PostMapping("register")
    public String login(@ModelAttribute("userDto") @Valid CreateUserDto userDto,
                        BindingResult bindingResult,
                        Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "auth/register";
        }

        userService.registerUser(userDto);
        return "redirect:/auth/login";
    }

    @GetMapping("login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            Locale locale = LocaleContextHolder.getLocale();
            model.addAttribute("error", messageSource.getMessage("auth.login.error", null, locale));
        }
        return "auth/login";
    }

    @GetMapping("forgot-password")
    public String showForgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("forgot-password")
    public String forgotPassword(HttpServletRequest req, Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        try{
            userService.makeResetPasswordLink(req);
            model.addAttribute("message", messageSource.getMessage("auth.forgot.success", null, locale));
        } catch (UserNotFoundException | IOException | IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
        } catch (MessagingException e) {
            model.addAttribute("error", messageSource.getMessage("auth.forgot.email.error", null, locale));
        }
        return "auth/forgot-password";
    }

    @GetMapping("reset-password")
    public String showResetPasswordPage(@RequestParam String token,
                                        Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        try{
            userService.getUserByResetPasswordToken(token);
            model.addAttribute("token", token);
        } catch (UserNotFoundException ex) {
            model.addAttribute("error", messageSource.getMessage("auth.reset.token.invalid", null, locale));
        }
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        try {
            UserDto user = userService.getUserByResetPasswordToken(token);
            userService.updatePassword(user, password);
            model.addAttribute("message", messageSource.getMessage("auth.reset.success", null, locale));
        } catch (UserNotFoundException ex) {
            model.addAttribute("error", messageSource.getMessage("auth.reset.token.invalid", null, locale));
            return "auth/reset-password";
        } catch (InvalidPasswordException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("token", token);
            return "auth/reset-password";
        }
        return "auth/message";
    }

}
