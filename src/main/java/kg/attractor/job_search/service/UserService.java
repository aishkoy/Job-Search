package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.dto.user.SimpleUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, Page<?>> getProfileListsPage(int page, int size, UserDto user);

    List<UserDto> getUsers();

    User getEntityById(Long userId);

    UserDto getUserById(Long userId);

    UserDto getUserByPhone(String phoneNumber);

    UserDto getUserByEmail(String email);

    List<UserDto> getUsersByName(String name);

    Long registerUser(CreateUserDto userDto);

    Long updateUser(Long userId, SimpleUserDto userDto);

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

    Page<UserDto> getApplicantPage(int page, int size);

    Page<UserDto> getEmployersPage(int page, int size);

    UserDto getAuthUser();

    SimpleUserDto mapToEditUser(UserDto userDto);

    Long getAuthId();

}
