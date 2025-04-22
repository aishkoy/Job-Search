package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.SimpleUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller("mvcProfile")
@RequestMapping("profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;


    @GetMapping()
    public String profile(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            Model model) {

        UserDto user = userService.getAuthUser();
        Pageable pageable = PageRequest.of(page - 1, size);

        model.addAttribute("user", user);
        model.addAttribute("size", size);

        model.addAttribute("resumesPage", toPage(user.getResumes(), pageable));
        model.addAttribute("vacanciesPage", toPage(user.getVacancies(), pageable));

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

    private <T> Page<T> toPage(List<T> list, Pageable pageable) {
        return new PageImpl<>(
                list.stream()
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .toList(),
                pageable,
                list.size()
        );
    }
}
