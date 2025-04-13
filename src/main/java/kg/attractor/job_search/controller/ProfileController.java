package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.EditUserDto;
import kg.attractor.job_search.service.ProfileService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller("mvcProfile")
@RequestMapping("profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;
    private final ProfileService profileService;

    @GetMapping()
    public String profile(Model model) {
        profileService.prepareProfileModel(model);
        return "profile/profile";
    }

    @GetMapping("edit")
    public String editProfile(Model model) {
        model.addAttribute("user", userService.getAuthUser());
        return "profile/edit-profile";
    }

    @PostMapping("avatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file) {
        userService.uploadAvatar(file, userService.getAuthId());
        return "redirect:/profile/edit";
    }

    @PostMapping("edit")
    public String editProfile(@ModelAttribute @Valid EditUserDto userDto) {
        userService.updateUser(userService.getAuthId(), userDto);
        return "redirect:/profile";
    }
}
