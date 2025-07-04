package kg.attractor.job_search.service.impl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.exception.nsee.ApplicantNotFoundException;
import kg.attractor.job_search.exception.nsee.EmployerNotFoundException;
import kg.attractor.job_search.exception.iae.InvalidPasswordException;
import kg.attractor.job_search.exception.nsee.UserNotFoundException;
import kg.attractor.job_search.mapper.UserMapper;
import kg.attractor.job_search.entity.User;
import kg.attractor.job_search.repository.UserRepository;
import kg.attractor.job_search.service.interfaces.RoleService;
import kg.attractor.job_search.service.interfaces.UserService;
import kg.attractor.job_search.util.CommonUtil;
import kg.attractor.job_search.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final MessageSource messageSource;

    @Override
    public User getEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("Не существует пользователя с таким id!"));
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userMapper.toDto(getEntityById(userId));
    }

    @Override
    public UserDto getUserByEmail(String email) {
        String userEmail = email.trim().toLowerCase();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("Не существует пользователя с таким email!"));
        log.info("Получен пользователь по email : {}", userEmail);
        return userMapper.toDto(user);
    }

    @Override
    public Long registerUser(CreateUserDto userDto) {
        roleService.getRoleById(userDto.getRole().getId());
        normalizeUserData(userDto);
        userDto.setPassword(encoder.encode(userDto.getPassword()));

        User user = userMapper.toEntity(userDto);
        userRepository.save(user);
        log.info("Зарегистрирован пользователь: {}", user.getId());
        return user.getId();
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
    public Long updateUser(Long userId, UserDto userDto) {
        if (!userId.equals(userDto.getId())) {
            throw new AccessDeniedException("Вы не имеете права на редактирование чужого профиля!");
        }

        UserDto dto = getUserById(userId);
        dto.setName(normalizeField(userDto.getName(), true));
        dto.setSurname(normalizeField(userDto.getSurname(), true));
        dto.setPhoneNumber(normalizeField(userDto.getPhoneNumber(), false));
        dto.setAge(userDto.getAge());

        User user = userMapper.toEntity(dto);
        userRepository.save(user);

        log.info("Обновлена информация о пользователе: {}", user.getId());

        return user.getId();
    }

    @Override
    public HttpStatus deleteUser(Long userId, Long authId) {
        getUserById(userId);
        if (!userId.equals(authId)) {
            throw new AccessDeniedException("Вы не имеете права удалять чужой профиль!");
        }
        userRepository.deleteById(userId);
        log.info("Удален пользователь: {}", userId);
        return HttpStatus.OK;
    }

    @Override
    public void updateUserLanguage(String email, String language) {
        userRepository.updateUserLanguage(email, language);
        log.info("Обновлен предпочитаемый язык пользователя: {}, язык: {}", email, language);
    }

    @Override
    public String getUserPreferredLanguage(String email) {
        return userRepository.findPreferredLanguageByEmail(email)
                .orElse("ru");
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDto getEmployerById(Long userId) {
        User user = userRepository.findEmployerById(userId).
                orElseThrow(() ->
                        new EmployerNotFoundException("Не существует работодателя с таким id!"));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getApplicantById(Long userId) {
        User user = userRepository.findApplicantById(userId)
                .orElseThrow(() ->
                        new ApplicantNotFoundException("Не существует соискателя с таким id!"));
        return userMapper.toDto(user);
    }

    @Override
    public MultipartFile uploadAvatar(MultipartFile file, Long authId) {
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Только файлы JPEG и PNG разрешены для загрузки");
        }

        userRepository.updateUserAvatar(authId, saveImage(file));
        return file;
    }

    @Override
    public MultipartFile uploadAvatar(Long userId, MultipartFile file, Long authId) {
        if (!userId.equals(authId)) {
            throw new AccessDeniedException("Вы не имеете права на загрузку аватара другому профилю!");
        }
        return uploadAvatar(file, authId);
    }

    @Override
    public Boolean existsUser(String email) {
        String userEmail = email.trim().toLowerCase();
        return userRepository.existsByEmail(userEmail);
    }

    @Override
    public ResponseEntity<?> getUserAvatar(Long userId) {
        UserDto userDto = getUserById(userId);

        if (userDto.getAvatar() == null || userDto.getAvatar().isEmpty()) {
            return FileUtil.getOutputFile("default_avatar.jpg", "defaults/", MediaType.IMAGE_JPEG);
        }

        String filename = userDto.getAvatar();
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        MediaType mediaType = extension.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;

        return FileUtil.getOutputFile(filename, "images/", mediaType);
    }

    public String saveImage(MultipartFile file) {
        return FileUtil.saveUploadFile(file, "images/");
    }

    @Override
    public Page<UserDto> getApplicantPage(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if(query == null || query.isBlank()){
            return getUserDtoPage(() -> userRepository.findApplicantPage(pageable),
                    "Не было найдено соискателей!");
        }

        return getUserDtoPage(() -> userRepository.findApplicantPageWithQuery(query, pageable),
                "Соискателей с таким именем не было найдено");
    }

    @Override
    public Page<UserDto> getEmployersPage(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if(query == null || query.isBlank()){
            return getUserDtoPage(() -> userRepository.findEmployerPage(pageable),
                    "Не было найдено работодателей!");
        }

        return getUserDtoPage(() -> userRepository.findEmployerPageWithQuery(query, pageable),
                "Работодателей с таким именем не было найдено");
    }

    private Page<UserDto> getUserDtoPage(Supplier<Page<User>> supplier, String notFoundMessage) {
        Page<User> userPage = supplier.get();
        if (userPage.isEmpty()) {
            throw new UserNotFoundException(notFoundMessage);
        }
        log.info("Получено {} вакансий на странице", userPage.getSize());
        return userPage.map(userMapper::toDto);
    }

    @Override
    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            Long currentUserId = ((CustomUserDetails) principal).getUserId();
            return userId.equals(currentUserId);
        }

        return false;
    }

    @Override
    public UserDto getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Auth object: {}", authentication);

        if (authentication == null) {
            log.error("Authentication is null");
            throw new NoSuchElementException("user not authorized");
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            log.error("Authentication is anonymous");
            throw new IllegalArgumentException("user not authorized");
        }

        String email = authentication.getName();
        return getUserByEmail(email);
    }

    @Override
    public Long getAuthId() {
        return getAuthUser().getId();
    }

    @Override
    public UserDto getUserByResetPasswordToken(String resetPasswordToken) {
        User user = userRepository.findByResetPasswordToken(resetPasswordToken)
                .orElseThrow(() -> new UserNotFoundException(
                        messageSource.getMessage("user.not.found.by.token", null, LocaleContextHolder.getLocale())
                ));
        return userMapper.toDto(user);
    }

    @Override
    public void updatePassword(UserDto userDto, String newPassword) {
        String passwordRegex = "^(?=.*[A-ZА-Я])(?=.*[a-zа-я])(?=.*\\d)[A-Za-zА-Яа-я\\d@#$%^&+=!]{8,}$";
        Locale locale = LocaleContextHolder.getLocale();

        if (newPassword == null || newPassword.isBlank()) {
            throw new InvalidPasswordException(
                    messageSource.getMessage("password.empty", null, locale)
            );
        }

        if (newPassword.length() < 8 || newPassword.length() > 20) {
            throw new InvalidPasswordException(
                    messageSource.getMessage("password.length", null, locale)
            );
        }

        if (!newPassword.matches(passwordRegex)) {
            throw new InvalidPasswordException(
                    messageSource.getMessage("password.pattern", null, locale)
            );
        }

        String password = encoder.encode(newPassword);
        userDto.setPassword(password);
        userDto.setResetPasswordToken(null);
        userRepository.save(userMapper.toEntity(userDto));
    }

    @Override
    public void makeResetPasswordLink(HttpServletRequest req) throws MessagingException, UserNotFoundException, IOException {
        String email = req.getParameter("email");
        Locale locale = LocaleContextHolder.getLocale();

        if (email.isBlank() || !isValidEmail(email)) {
            throw new IllegalArgumentException(
                    messageSource.getMessage("email.invalid.format", null, locale)
            );
        }

        if (Boolean.FALSE.equals(existsUser(email))) {
            throw new UserNotFoundException(
                    messageSource.getMessage("user.not.found.by.email", null, locale)
            );
        }

        String token = UUID.randomUUID().toString();
        updateResetPasswordToken(token, email);
        String resetPasswordLink = CommonUtil.getSiteUrl(req) + "/auth/reset-password?token=" + token;
        emailService.sendEmail(email, resetPasswordLink);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }

    private void updateResetPasswordToken(String token, String email) {
        UserDto userDto = getUserByEmail(email);
        userDto.setResetPasswordToken(token);
        userRepository.save(userMapper.toEntity(userDto));
    }
}