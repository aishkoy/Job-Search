package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.EditUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.ProfileService;
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
    private final ProfileService profileService;

    @GetMapping()
    public String profile(Model model) {
        profileService.prepareProfileModel(model);
        return "profile/profile";
    }

    @GetMapping("edit")
    public String editProfile(Model model) {
        UserDto currentUser = userService.getAuthUser();
        EditUserDto editUserDto = userService.mapToEditUser(currentUser);

        model.addAttribute("userDto", editUserDto);
        return "profile/edit-profile";
    }

    @PostMapping("avatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file) {
        userService.uploadAvatar(file, userService.getAuthId());
        return "redirect:/profile";
    }

    @PostMapping("edit")
    public String editProfile(@ModelAttribute("userDto") @Valid EditUserDto userDto,
                              BindingResult bindingResult,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.getAuthUser());
            return "profile/edit-profile";
        }

        userService.updateUser(userService.getAuthId(), userDto);
        return "redirect:/profile";
    }
}
