package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.UserDao;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.exceptions.IncorrectUserEmailException;
import kg.attractor.job_search.exceptions.id.IncorrectUserIdException;
import kg.attractor.job_search.exceptions.notFound.UserNotFoundException;
import kg.attractor.job_search.mapper.UserMapper;
import kg.attractor.job_search.models.User;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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
    public Optional<UserDto> getUserById(Long userId) {
        return userDao.getUserById(userId)
                .map(UserMapper::toUserDto);
    }

    @Override
    public Optional<UserDto> getUserByPhone(String phoneNumber){
        String phone = phoneNumber.trim().toLowerCase();
        return userDao.getUserByPhone(phone)
                .map(UserMapper::toUserDto);
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email){
        String userEmail = email.trim().toLowerCase();
        return userDao.getUserByEmail(userEmail)
                .map(UserMapper::toUserDto);
    }

    @Override
    public List<UserDto> getUsersByName(String userName){
        String name = userName.trim().toLowerCase();
        name = StringUtils.capitalize(name);
        return userDao.getUsersByName(name)
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public Long registerUser(UserDto userDto,boolean isEmployer) {
        if (userDto.getEmail().isBlank() || userDto.getName().isBlank()) {
            throw new IllegalArgumentException("Email и имя обязательны");
        }

        if(Boolean.TRUE.equals(userDao.existsUserByEmail(userDto.getEmail()))) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        String userName = userDto.getName().trim().toLowerCase();
        userName = StringUtils.capitalize(userName);

        userDto.setName(userName);
        userDto.setEmail(userDto.getEmail().trim().toLowerCase());
        userDto.setPhoneNumber(userDto.getPhoneNumber().trim().toLowerCase());
        userDto.setAccountType(isEmployer ? "employer" : "applicant");

        return userDao.registerUser(UserMapper.toUser(userDto));
    }

    @Override
    public Long updateUser(Long userId, UserDto userDto) {
        Optional<UserDto> user = getUserById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Вы не можете обновить информацию о несуществующем пользователе");
        }

        if (!userDto.getId().equals(userId)) {
            throw new IncorrectUserIdException("Неправильный id в теле запроса");
        }

        user.ifPresent(u -> {
            if(Boolean.TRUE.equals(userDao.existsUserByEmail(u.getEmail()))) {
                throw new IncorrectUserEmailException("Вы не можете изменить email на email уже существующего пользователя!");
            }
        });

        String name = userDto.getName().trim().toLowerCase();
        name = StringUtils.capitalize(name);
        userDto.setName(name);
        userDto.setEmail(userDto.getEmail().trim().toLowerCase());
        userDto.setPhoneNumber(userDto.getPhoneNumber().trim().toLowerCase());

        return userDao.updateUser(UserMapper.toUser(userDto));
    }

    @Override
    public List<UserDto> getEmployers() {
        return userDao.getEmployers()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }


    @Override
    public Optional<UserDto> getEmployerById(Long userId) {
        return userDao.getEmployerById(userId).map(UserMapper::toUserDto);
    }

    @Override
    public List<UserDto> getApplicants() {
        return userDao.getApplicants()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public Optional<UserDto> getApplicantById(Long userId) {
        return userDao.getApplicantById(userId).map(UserMapper::toUserDto);
    }

    @Override
    public MultipartFile uploadAvatar(Long userId, MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Только файлы JPEG и PNG разрешены для загрузки");
        }

        User user = userDao.getUserById(userId).orElse(null);
        if(user == null){
            throw new UserNotFoundException();
        }
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
    public Boolean existsUser(String email){
        String userEmail = email.trim().toLowerCase();
        return userDao.existsUserByEmail(userEmail);
    }

    @Override
    public ResponseEntity<?> getUserAvatar(Long userId) {
        UserDto userDto = userDao.getUserById(userId).map(UserMapper::toUserDto).orElse(null);

        if(userDto == null){
            throw new UserNotFoundException();
        }

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
