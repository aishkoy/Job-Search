package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();
    UserDto getUserById(Long userId);
    Long registerUser(UserDto userDto,  boolean isEmployer);
    Long updateUser(Long userId, UserDto userDto);

    List<UserDto> getEmployers();

    UserDto getEmployerById(Long id);

    List<UserDto> getApplicants();

    UserDto getApplicantById(Long id);

    MultipartFile uploadAvatar(Long userId, MultipartFile file);

    List<UserDto> getApplicationsByVacancyId(Long vacancyId);

    ResponseEntity<?> getUserAvatar(Long userId);
}
