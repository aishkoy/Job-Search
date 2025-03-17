package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        // TODO получение всех юзеров (компании и соискатели)
        return ResponseEntity.ofNullable(userService.getUsers());
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") Long userId) {
        // TODO получение юзера по id (компании и соискатели)
        return  ResponseEntity.ofNullable(userService.getUserById(userId));
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> updateUser(@PathVariable("id") Long userId ,@RequestBody UserDto userDto) {
        // TODO обновление информации юзера
        return ResponseEntity.ofNullable(userService.updateUser(userId, userDto));
    }

    @PostMapping("{id}/avatar")
    // TODO загрузка аватара пользователя
    public ResponseEntity<MultipartFile> uploadAvatar(@PathVariable("id") Long userId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ofNullable(userService.uploadAvatar(userId, file));
    }

    @GetMapping("employers")
    public ResponseEntity<List<UserDto>> getEmployers() {
        // TODO получить всех работодателей для соискателя
        return ResponseEntity.ofNullable(userService.getEmployers());
    }

    @GetMapping("employers/{id}")
    public ResponseEntity<UserDto> getEmployerById(@PathVariable("id") Long id) {
        //TODO получить конкретного работодателя
        return ResponseEntity.ofNullable(userService.getEmployerById(id));
    }

    @GetMapping("applicants")
    public ResponseEntity<List<UserDto>> getApplicants() {
        // TODO получить всех соискателей для работодателя
        return ResponseEntity.of(Optional.ofNullable(userService.getApplicants()));
    }

    @GetMapping("applicants/{id}")
    public ResponseEntity<UserDto> getApplicantById(@PathVariable("id") Long id) {
        //TODO получить конкретного соискателя
        return ResponseEntity.of(Optional.ofNullable(userService.getApplicantById(id)));
    }
}
