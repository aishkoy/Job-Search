package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.dto.user.EditUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    UserDto getUserById(Long userId);

    UserDto getUserByPhone(String phoneNumber);

    UserDto getUserByEmail(String email);

    List<UserDto> getUsersByName(String name);

    Long registerUser(CreateUserDto userDto);

    Long updateUser(Long userId, EditUserDto userDto);

    HttpStatus deleteUser(Long userId, Long authId);

    List<UserDto> getEmployers();

    UserDto getEmployerById(Long id);

    List<UserDto> getApplicants();

    UserDto getApplicantById(Long id);

    MultipartFile uploadAvatar(MultipartFile file, Long authId);

    MultipartFile uploadAvatar(Long userId, MultipartFile file, Long authId);

    List<UserDto> getApplicationsByVacancyId(Long vacancyId);

    Boolean existsUser(String email);

    ResponseEntity<?> getUserAvatar(Long userId);

    UserDto getAuthUser();

    Long getAuthId();

    String getUserName(Long userId);
}
