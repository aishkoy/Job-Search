package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<Long> register(@RequestBody UserDto userDto,  @RequestParam boolean isEmployer) {
        return ResponseEntity.ofNullable(userService.registerUser(userDto, isEmployer));
    }
}
