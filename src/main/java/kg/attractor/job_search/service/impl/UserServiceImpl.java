package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.UserDao;
import kg.attractor.job_search.dto.user.EditUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.exception.ApplicantNotFoundException;
import kg.attractor.job_search.exception.EmployerNotFoundException;
import kg.attractor.job_search.exception.IncorrectUserEmailException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.mapper.UserMapper;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public List<UserDto> getUsers() {
        return userDao.getUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userDao.getUserById(userId).orElseThrow(() -> new UserNotFoundException("Не существует пользователя с таким id!"));
        log.info("Retrieved user by id: {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getUserByPhone(String phoneNumber) {
        String phone = phoneNumber.trim().toLowerCase();
        User user = userDao.getUserByPhone(phone).orElseThrow(() -> new UserNotFoundException("Не существует пользователя с таким номером телефона!"));
        log.info("Retrieved user by name: {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        String userEmail = email.trim().toLowerCase();
        User user = userDao.getUserByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("Не существует пользователя с таким email!"));
        log.info("Retrieved user by email : {}", userEmail);
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getUsersByName(String userName) {
        String name = userName.trim().toLowerCase();
        name = StringUtils.capitalize(name);
        List<UserDto> users =  userDao.getUsersByName(name)
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
        if(users.isEmpty()) {
            throw new UserNotFoundException("Не существует пользователей с таким именем!");
        }
        return users;
    }

    @Override
    public Long registerUser(UserDto userDto, boolean isEmployer) {
        if (userDto.getEmail().isBlank() || userDto.getName().isBlank()) {
            throw new IncorrectUserEmailException("Email и имя обязательны");
        }

        if (Boolean.TRUE.equals(userDao.existsUserByEmail(userDto.getEmail()))) {
            throw new IncorrectUserEmailException("Пользователь с таким email уже существует");
        }

        String userName = userDto.getName().trim().toLowerCase();
        userName = StringUtils.capitalize(userName);

        userDto.setName(userName);
        userDto.setEmail(userDto.getEmail().trim().toLowerCase());
        userDto.setPhoneNumber(userDto.getPhoneNumber().trim().toLowerCase());
        userDto.setAccountType(isEmployer ? "employer" : "applicant");

        Long id = userDao.registerUser(UserMapper.toUser(userDto)) ;
        log.info("Register user: {}", id);
        return id;
    }

    @Override
    public Long updateUser(Long userId, EditUserDto userDto) {
        getUserById(userId);

        String name = userDto.getName().trim().toLowerCase();
        name = StringUtils.capitalize(name);

        userDto.setName(name);
        userDto.setPhoneNumber(userDto.getPhoneNumber().trim().toLowerCase());

        User user = UserMapper.toUser(userDto);
        user.setId(userId);
        Long id =  userDao.updateUser(user);

        log.info("Updated user: {}", id);

        return id;
    }

    @Override
    public HttpStatus deleteUser(Long userId){
        getUserById(userId);
        userDao.deleteUser(userId);
        log.info("Deleted user: {}", userId);
        return HttpStatus.OK;
    }

    @Override
    public List<UserDto> getEmployers() {
        return userDao.getEmployers()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }


    @Override
    public UserDto getEmployerById(Long userId) {
        User user = userDao.getEmployerById(userId).orElseThrow(() -> new EmployerNotFoundException("Не существует работодателя с таким id!"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getApplicants() {
        return userDao.getApplicants()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getApplicantById(Long userId) {
        User user = userDao.getApplicantById(userId).orElseThrow(() -> new ApplicantNotFoundException("Не существует соискателя с таким id!"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public MultipartFile uploadAvatar(Long userId, MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Только файлы JPEG и PNG разрешены для загрузки");
        }

        User user = userDao.getUserById(userId).orElseThrow(UserNotFoundException::new);
        user.setAvatar(saveImage(file));
        userDao.updateUser(user);
        return file;
    }

    @Override
    public List<UserDto> getApplicationsByVacancyId(Long vacancyId) {
        return userDao.getApplicantsByVacancyId(vacancyId)
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public Boolean existsUser(String email) {
        String userEmail = email.trim().toLowerCase();
        return userDao.existsUserByEmail(userEmail);
    }

    @Override
    public ResponseEntity<?> getUserAvatar(Long userId) {
        UserDto userDto = getUserById(userId);

        if (userDto.getAvatar() == null) {
            return ResponseEntity.notFound().build();
        }

        String filename = userDto.getAvatar();
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        MediaType mediaType = extension.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;

        return FileUtil.getOutputFile(filename, "images/", mediaType);
    }

    public String saveImage(MultipartFile file) {
        return FileUtil.saveUploadFile(file, "images/");
    }
}
