package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.SimpleUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller("mvcProfile")
@RequestMapping("profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping()
    public String profile(Model model) {
        model.addAttribute("user", userService.getAuthUser());
        return "profile/profile";
    }

    @GetMapping("edit")
    public String editProfile(Model model) {
        UserDto currentUser = userService.getAuthUser();
        SimpleUserDto simpleUserDto = userService.mapToEditUser(currentUser);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userDto", simpleUserDto);
        return "profile/edit-profile";
    }

    @PostMapping("avatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file) {
        userService.uploadAvatar(file, userService.getAuthId());
        return "redirect:/profile";
    }

    @PostMapping("edit")
    public String editProfile(@ModelAttribute("userDto") @Valid SimpleUserDto userDto,
                              BindingResult bindingResult,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("currentUser", userService.getAuthUser());
            return "profile/edit-profile";
        }

        userService.updateUser(userService.getAuthId(), userDto);
        return "redirect:/profile";
    }
}
