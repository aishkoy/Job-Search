package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.UserDao;
import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.dto.user.EditUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.exception.ApplicantNotFoundException;
import kg.attractor.job_search.exception.EmployerNotFoundException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.mapper.UserMapper;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.RoleService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder encoder;

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> users = userDao.getUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();

        validateUsersList(users, "Пока никто не зарегистрирован");
        return users;
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userDao.getUserById(userId).orElseThrow(() -> new UserNotFoundException("Не существует пользователя с таким id!"));
        log.info("Получено резюме по id: {}", user.getId());
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUserByPhone(String phoneNumber) {
        String phone = phoneNumber.trim().toLowerCase();
        User user = userDao.getUserByPhone(phone).orElseThrow(() -> new UserNotFoundException("Не существует пользователя с таким номером телефона!"));
        log.info("Получен пользователь по имени: {}", user.getId());
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        String userEmail = email.trim().toLowerCase();
        User user = userDao.getUserByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("Не существует пользователя с таким email!"));
        log.info("Получен пользователь по email : {}", userEmail);
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getUsersByName(String userName) {
        String name = userName.trim().toLowerCase();
        name = StringUtils.capitalize(name);
        List<UserDto> users = userDao.getUsersByName(name)
                .stream()
                .map(userMapper::toDto)
                .toList();

        validateUsersList(users, "Не существует пользователей с таким именем!");
        return users;
    }

    @Override
    public Long registerUser(CreateUserDto userDto) {
        if (Boolean.TRUE.equals(userDao.existsUserByEmail(userDto.getEmail()))) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        roleService.getRoleId(userDto.getRoleId());
        normalizeUserData(userDto);
        userDto.setPassword(encoder.encode(userDto.getPassword()));

        Long id = userDao.registerUser(userMapper.toEntity(userDto)) ;
        log.info("Зарегистрирован пользователь: {}", id);
        return id;
    }

    private void normalizeUserData(CreateUserDto dto) {
        dto.setName(normalizeField(dto.getName(), true));
        dto.setSurname(normalizeField(dto.getSurname(), true));
        dto.setEmail(normalizeField(dto.getEmail(), false));
        dto.setPhoneNumber(normalizeField(dto.getPhoneNumber(), false));
    }

    private String normalizeField(String field, boolean capitalize) {
        if (field == null || field.isBlank()) {
            return null;
        }

        String normalized = field.trim().toLowerCase();
        return capitalize ? StringUtils.capitalize(normalized) : normalized;
    }

    @Override
    public Long updateUser(Long userId, EditUserDto userDto) {
        getUserById(userId);

        if(!userId.equals(userDto.getId())) {
            throw new AccessDeniedException("Вы не имеете права на редактироание чужого профиля!");
        }

        userDto.setName(normalizeField(userDto.getName(), true));
        userDto.setSurname(normalizeField(userDto.getSurname(), true));
        userDto.setPhoneNumber(normalizeField(userDto.getPhoneNumber(), false));

        User user = userMapper.toEntity(userDto);
        user.setId(userId);
        Long id = userDao.updateUser(user);

        log.info("Обновлена информация о пользователе: {}", id);

        return id;
    }

    @Override
    public HttpStatus deleteUser(Long userId, Long authId) {
        getUserById(userId);
        if(!userId.equals(authId)) {
            throw new AccessDeniedException("Вы не имеете права удалять чужжой профиль!");
        }
        userDao.deleteUser(userId);
        log.info("Удален пользователь: {}", userId);
        return HttpStatus.OK;
    }

    @Override
    public List<UserDto> getEmployers() {
        List<UserDto> employers = userDao.getEmployers()
                .stream()
                .map(userMapper::toDto)
                .toList();

        validateUsersList(employers, "Работодатели не найдены");
        return employers;
    }

    @Override
    public UserDto getEmployerById(Long userId) {
        User user = userDao.getEmployerById(userId).orElseThrow(() -> new EmployerNotFoundException("Не существует работодателя с таким id!"));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getApplicants() {
        List<UserDto> applicants = userDao.getApplicants()
                .stream()
                .map(userMapper::toDto)
                .toList();

        validateUsersList(applicants, "Соискатели не найдены");
        return applicants;
    }

    @Override
    public UserDto getApplicantById(Long userId) {
        User user = userDao.getApplicantById(userId).orElseThrow(() -> new ApplicantNotFoundException("Не существует соискателя с таким id!"));
        return userMapper.toDto(user);
    }

    @Override
    public MultipartFile uploadAvatar(Long userId, MultipartFile file, Long authId) {
        if(!userId.equals(authId)) {
            throw new AccessDeniedException("Вы не имеете права на загрузку аватара другому профилю!");
        }
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
        List<UserDto> applications = userDao.getApplicantsByVacancyId(vacancyId)
                .stream()
                .map(userMapper::toDto)
                .toList();

        validateUsersList(applications, "Нет соискателей, откликнувшихся на данную вакансию");
        return applications;
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
    private void validateUsersList(List<UserDto> users, String errorMessage) {
        if (users.isEmpty()) {
            log.warn(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
        log.info("Получено {} пользователей", users.size());
    }

    @Override
    public UserDto getAuthUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Auth object: {}", authentication);
        if (authentication != null) {
            log.info("Auth type: {}, name: {}, isAuthenticated: {}",
                    authentication.getClass().getName(),
                    authentication.getName(),
                    authentication.isAuthenticated());
            log.info("Authorities: {}", authentication.getAuthorities());
        }

        if (authentication == null) {
            log.error("Authentication is null");
            throw new NoSuchElementException("user not authorized");
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            log.error("Authentication is anonymous");
            throw new IllegalArgumentException("user not authorized");
        }

        String email = authentication.getName();
        log.info("Looking up user by email: {}", email);
        return getUserByEmail(email);
    }

    @Override
    public Long getAuthId(){
        return getAuthUser().getId();
    }
}