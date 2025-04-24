package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.SimpleUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public abstract class UserController {
    protected final UserService userService;

    protected abstract UserDto getUserById(Long userId);

    protected String getUrl() {
        RequestMapping mapping = this.getClass().getAnnotation(RequestMapping.class);
        return mapping != null && mapping.value().length > 0
                ? mapping.value()[0].replace("/", "")
                : "";
    }

    @GetMapping("{userId}")
    public String profile(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            Model model) {

        UserDto user = getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("size", size);
        model.addAttribute("url", getUrl());
        model.addAllAttributes(userService.getProfileListsPage(page, size, user));

        return "profile/profile";
    }

    @GetMapping("{userId}/edit")
    public String editProfile(@PathVariable Long userId,
                              Model model) {
        UserDto user = getUserById(userId);
        SimpleUserDto simpleUserDto = userService.mapToEditUser(user);

        model.addAttribute("url", getUrl());
        model.addAttribute("userDto", simpleUserDto);
        return "profile/edit-profile";
    }

    @PostMapping("{userId}/avatar")
    public String uploadAvatar(@PathVariable Long userId,
                               @RequestParam("file") MultipartFile file) {
        userService.uploadAvatar(file, userId);
        return "redirect:/%s/%d".formatted(getUrl(), userId);
    }

    @PostMapping("{userId}/edit")
    public String editProfile(@PathVariable Long userId,
                              @ModelAttribute("userDto") @Valid SimpleUserDto userDto,
                              BindingResult bindingResult,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("url", getUrl());
            return "profile/edit-profile";
        }

        userService.updateUser(userId, userDto);
        return "redirect:/%s/%d".formatted(getUrl(), userId);
    }

}
