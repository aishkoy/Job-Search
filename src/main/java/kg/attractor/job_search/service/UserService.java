package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getUsers();
    Optional<UserDto> getUserById(Long userId);

    Optional<UserDto> getUserByPhone(String phoneNumber);

    Optional<UserDto> getUserByEmail(String email);

    List<UserDto> getUsersByName(String name);

    Long registerUser(UserDto userDto, boolean isEmployer);
    Long updateUser(Long userId, UserDto userDto);

    List<UserDto> getEmployers();

    Optional<UserDto> getEmployerById(Long id);

    List<UserDto> getApplicants();

    Optional<UserDto> getApplicantById(Long id);

    MultipartFile uploadAvatar(Long userId, MultipartFile file);

    List<UserDto> getApplicationsByVacancyId(Long vacancyId);

    Boolean existsUser(String email);

    ResponseEntity<?> getUserAvatar(Long userId);
}
