package kg.attractor.job_search.service.interfaces;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    User getEntityById(Long userId);

    UserDto getUserById(Long userId);

    UserDto getUserByEmail(String email);

    UserDto getEmployerById(Long id);

    UserDto getApplicantById(Long id);

    UserDto getUserByResetPasswordToken(String resetPasswordToken);

    UserDto getAuthUser();

    Long getAuthId();

    Long registerUser(CreateUserDto userDto);

    Long updateUser(Long userId, UserDto userDto);

    String getUserPreferredLanguage(String email);

    HttpStatus deleteUser(Long userId, Long authId);

    MultipartFile uploadAvatar(MultipartFile file, Long authId);

    MultipartFile uploadAvatar(Long userId, MultipartFile file, Long authId);

    ResponseEntity<?> getUserAvatar(Long userId);

    Page<UserDto> getApplicantPage(String query, int page, int size);

    Page<UserDto> getEmployersPage(String query, int page, int size);

    Boolean existsUser(String email);

    boolean isCurrentUser(Long userId);

    void save(User user);

    void updateUserLanguage(String email, String language);

    void updatePassword(UserDto userDto, String newPassword);

    void makeResetPasswordLink(HttpServletRequest req) throws MessagingException, IOException;
}
