package kg.attractor.job_search.controller.api;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

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

    @GetMapping("current")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ofNullable(userService.getAuthUser());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId) {
        return ResponseEntity.ofNullable(userService.getUserById(userId));
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateUser(@PathVariable("id") Long userId, @RequestBody @Valid UserDto userDto) {
        userDto.setId(userService.getAuthId());
        return ResponseEntity.ofNullable(userService.updateUser(userId, userDto));
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteUser(@PathVariable("id") Long userId) {
        return userService.deleteUser(userId, userService.getAuthId());
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
    public ResponseEntity<Page<UserDto>> getEmployers(@RequestParam(required = false) String query,
                                                      @RequestParam(required = false, defaultValue = "1") int page,
                                                      @RequestParam(required = false, defaultValue = "5") int size) {
        return ResponseEntity.ofNullable(userService.getEmployersPage(query, page, size));
    }

    @GetMapping("employers/{id}")
    public ResponseEntity<UserDto> getEmployerById(@PathVariable("id") Long id) {
        return ResponseEntity.ofNullable(userService.getEmployerById(id));
    }

    @GetMapping("applicants")
    public ResponseEntity<Page<UserDto>> getApplicants(@RequestParam(required = false) String query,
                                                       @RequestParam(required = false, defaultValue = "1") int page,
                                                       @RequestParam(required = false, defaultValue = "5") int size) {
        return ResponseEntity.ofNullable(userService.getApplicantPage(query, page, size));
    }

    @GetMapping("applicants/{id}")
    public ResponseEntity<UserDto> getApplicantById(@PathVariable("id") Long id) {
        return ResponseEntity.ofNullable(userService.getApplicantById(id));
    }
}
