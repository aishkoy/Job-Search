package kg.attractor.job_search.service.impl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.user.CreateUserDto;
import kg.attractor.job_search.dto.user.SimpleUserDto;
import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.exception.ApplicantNotFoundException;
import kg.attractor.job_search.exception.EmployerNotFoundException;
import kg.attractor.job_search.exception.InvalidPasswordException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.mapper.UserMapper;
import kg.attractor.job_search.entity.User;
import kg.attractor.job_search.repository.UserRepository;
import kg.attractor.job_search.service.RoleService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.util.CommonUtil;
import kg.attractor.job_search.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public Map<String, Page<?>> getProfileListsPage(int page, int size, UserDto user) {
        Map<String, Page<?>> template = new HashMap<>();

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<ResumeDto> resumes = toPage(user.getResumes(), pageable);
        Page<VacancyDto> vacancies = toPage(user.getVacancies(), pageable);

        template.put("resumesPage", resumes);
        template.put("vacanciesPage", vacancies);
        return template;
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();

        validateUsersList(users, "Пока никто не зарегистрирован");
        return users;
    }

    @Override
    public User getEntityById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("Не существует пользователя с таким id!"));
        log.info("Получен пользователь по id: {}", user.getId());
        return user;
    }


    @Override
    public UserDto getUserById(Long userId) {
        return userMapper.toDto(getEntityById(userId));
    }

    @Override
    public UserDto getUserByPhone(String phoneNumber) {
        String phone = phoneNumber.trim().toLowerCase();
        User user = userRepository.findByPhoneNumber(phone)
                .orElseThrow(() ->
                        new UserNotFoundException("Не существует пользователя с таким номером телефона!"));
        log.info("Получен пользователь по номеру: {}", phone);
        return userMapper.toDto(user);
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
    public List<UserDto> getUsersByName(String userName) {
        String name = userName.trim().toLowerCase();
        name = StringUtils.capitalize(name);
        List<UserDto> users = userRepository.findAllByName(name)
                .stream()
                .map(userMapper::toDto)
                .toList();

        validateUsersList(users, "Не существует пользователей с таким именем!");
        return users;
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
    public Long updateUser(Long userId, SimpleUserDto userDto) {
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
    public void updateUserLanguage(String email, String language){
        userRepository.updateUserLanguage(email, language);
        log.info("Обновлен предпочитаемый язык пользователя: {}, язык: {}", email, language);
    }

    @Override
    public String getUserPreferredLanguage(String email) {
        return userRepository.findPreferredLanguageByEmail(email)
                .orElse("ru");
    }

    @Override
    public void save(User user){
        userRepository.save(user);
    }

    @Override
    public List<UserDto> getEmployers() {
        List<UserDto> employers = userRepository.findEmployers()
                .stream()
                .map(userMapper::toDto)
                .toList();

        validateUsersList(employers, "Работодатели не найдены");
        return employers;
    }

    @Override
    public UserDto getEmployerById(Long userId) {
        User user = userRepository.findEmployerById(userId).
                orElseThrow(() ->
                        new EmployerNotFoundException("Не существует работодателя с таким id!"));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getApplicants() {
        List<UserDto> applicants = userRepository.findApplicants()
                .stream()
                .map(userMapper::toDto)
                .toList();

        validateUsersList(applicants, "Соискатели не найдены");
        return applicants;
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
    public List<UserDto> getApplicationsByVacancyId(Long vacancyId) {
        List<UserDto> applications =
                userRepository.findApplicantsByVacancyId(vacancyId)
                        .stream()
                        .map(userMapper::toDto)
                        .toList();

        validateUsersList(applications, "Нет соискателей, откликнувшихся на данную вакансию");
        return applications;
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
            return FileUtil.getStaticFile("default_avatar.jpg", "images/", MediaType.IMAGE_JPEG);
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
    public Page<UserDto> getApplicantPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return getUserDtoPage(() -> userRepository.findAllApplicantsPage(pageable),
                "Не было найдено соискателей!");
    }

    @Override
    public Page<UserDto> getEmployersPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return getUserDtoPage(() -> userRepository.findAllEmployersPage(pageable),
                "Не было найдено компаний!");
    }

    private Page<UserDto> getUserDtoPage(Supplier<Page<User>> supplier, String notFoundMessage) {
        Page<User> userPage = supplier.get();
        if (userPage.isEmpty()) {
            throw new UserNotFoundException(notFoundMessage);
        }
        log.info("Получено {} вакансий на странице", userPage.getSize());
        return userPage.map(userMapper::toDto);
    }

    private <T> Page<T> toPage(List<T> list, Pageable pageable) {
        return new PageImpl<>(
                list.stream()
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .toList(),
                pageable,
                list.size()
        );
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
    public SimpleUserDto mapToEditUser(UserDto userDto) {
        return userMapper.toSimpleUserDto(userDto);
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
        String passwordRegex = "^(?=.*[A-Za-zА-Яа-я])(?=.*\\d)[A-Za-zА-Яа-я\\d@#$%^&+=!]{8,20}$";
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