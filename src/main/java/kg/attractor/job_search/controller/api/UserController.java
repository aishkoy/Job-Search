package kg.attractor.job_search.controller.api;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ofNullable(userService.getUsers());
    }

    @PostMapping("/language")
    @Transactional
    public ResponseEntity<Void> updateLanguage(@RequestParam String lang) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                userService.updateUserLanguage(email, lang);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId) {
        return  ResponseEntity.ofNullable(userService.getUserById(userId));
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateUser(@PathVariable("id") Long userId ,@RequestBody @Valid UserDto userDto) {
        userDto.setId(userService.getAuthId());
        return ResponseEntity.ofNullable(userService.updateUser(userId, userDto));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteUser(@PathVariable("id") Long userId) {
        return userService.deleteUser(userId, userService.getAuthId());
    }

    @GetMapping("by-name")
    public ResponseEntity<List<UserDto>> getUsersByName(@RequestParam String name) {
        return ResponseEntity.ofNullable(userService.getUsersByName(name));
    }

    @GetMapping("by-phone")
    public ResponseEntity<UserDto> getUserByPhone(@RequestParam String phoneNumber) {
        return  ResponseEntity.ofNullable(userService.getUserByPhone(phoneNumber));
    }

    @GetMapping("by-email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        return  ResponseEntity.ofNullable(userService.getUserByEmail(email));
    }

    @GetMapping("exists")
    public ResponseEntity<Boolean> getUserExists(@RequestParam String email) {
        return ResponseEntity.ofNullable(userService.existsUser(email));
    }

    @PostMapping("{id}/avatar")
    public ResponseEntity<MultipartFile> uploadAvatar(@PathVariable("id") Long userId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ofNullable(userService.uploadAvatar(userId, file, userService.getAuthId()));
    }

    @GetMapping("{id}/avatar")
    public ResponseEntity<?> getAvatar(@PathVariable("id") Long userId) {
        return userService.getUserAvatar(userId);
    }


    @GetMapping("employers")
    public ResponseEntity<List<UserDto>> getEmployers() {
        return ResponseEntity.ofNullable(userService.getEmployers());
    }

    @GetMapping("employers/{id}")
    public ResponseEntity<UserDto> getEmployerById(@PathVariable("id") Long id) {
        return  ResponseEntity.ofNullable(userService.getEmployerById(id));
    }

    @GetMapping("applicants")
    public ResponseEntity<List<UserDto>> getApplicants() {
        return ResponseEntity.ofNullable(userService.getApplicants());
    }

    @GetMapping("applicants/{id}")
    public ResponseEntity<UserDto> getApplicantById(@PathVariable("id") Long id) {
        return ResponseEntity.ofNullable(userService.getApplicantById(id));
    }

    @GetMapping("responded/{vacancyId}")
    public ResponseEntity<List<UserDto>> getApplications(@PathVariable("vacancyId") Long vacancyId) {
        return ResponseEntity.ofNullable(userService.getApplicationsByVacancyId(vacancyId));
    }
}
