package kg.attractor.job_search.controller.api;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @PostMapping("register")
    public ResponseEntity<Long> register(@RequestBody @Valid CreateUserDto userDto) {
        return ResponseEntity.ofNullable(userService.registerUser(userDto));
    }
}
