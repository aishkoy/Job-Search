package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.exceptions.UserNotFoundException;
import kg.attractor.job_search.mapper.UserMapper;
import kg.attractor.job_search.models.User;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.util.FileUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class UserServiceImpl implements UserService {
    private final List<User> users;

    public UserServiceImpl(FileUtil fileUtil) {
        this.users = fileUtil.getUsers("users.json");
    }

    @Override
    public List<UserDto> getUsers() {
        // TODO получение всех юзеров (компании и соискатели)
        return users.stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long userId) {
        // TODO получение юзера по id
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .map(UserMapper::toUserDto)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public Long registerUser(UserDto userDto,boolean isEmployer) {
        // TODO создание юзера (работодатель/соискатель)
        userDto.setAccountType(isEmployer ? "Employer" : "Applicant");
        users.add(UserMapper.toUser(userDto));
        return userDto.getId();
    }

    @Override
    public Long updateUser(Long userId, UserDto userDto) {
        // TODO обновление информации юзера
        int userIndex = IntStream.range(0, users.size())
                .filter(index -> users.get(index).getId().equals(userId))
                .findFirst()
                .orElse(-1);

        if (!userDto.getId().equals(userId)) {
            return null;
        }

        users.set(userIndex, UserMapper.toUser(userDto));
        return userDto.getId();
    }

    @Override
    public List<UserDto> getEmployers() {
        // TODO получить всех работодателей для соискателя
        return users.stream()
                .filter(user -> user.getAccountType().equalsIgnoreCase("Employer"))
                .map(UserMapper::toUserDto)
                .toList();
    }


    @Override
    public UserDto getEmployerById(Long userId) {
        //TODO получить конкретного работодателя
        return users.stream()
                .filter(user -> user.getAccountType().equalsIgnoreCase("Employer") && user.getId().equals(userId))
                .map(UserMapper::toUserDto)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public List<UserDto> getApplicants() {
        // TODO получить всех соискателей для работодателя
        return users.stream()
                .filter(user -> user.getAccountType().equalsIgnoreCase("Applicant"))
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getApplicantById(Long userId) {
        //TODO получить конкретного соискателя
        return users.stream()
                .filter(user -> user.getAccountType().equalsIgnoreCase("Applicant") && user.getId().equals(userId))
                .map(UserMapper::toUserDto)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public MultipartFile uploadAvatar(Long userId, MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Только файлы JPEG и PNG разрешены для загрузки");
        }

        // TODO загрузка аватара пользователя
        UserDto userDto = getUserById(userId);
        userDto.setAvatar(saveImage(file));
        updateUser(userId, userDto);
        return file;
    }

    @Override
    //TODO получение автара пользователя (доработать)
    public ResponseEntity<?> getUserAvatar(Long userId) {
        UserDto userDto = getUserById(userId);
        if(userDto == null){
            throw  new UserNotFoundException();
        }

        if (userDto.getAvatar() == null) {
            return ResponseEntity.notFound().build();
        }

        FileUtil fu  = new FileUtil();
        String filename = userDto.getAvatar();
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        MediaType mediaType = extension.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;

        return fu.getOutputFile(filename, "images/", mediaType);
    }

    public String saveImage(MultipartFile file) {
        // TODO сохранение картинки
        FileUtil fu = new FileUtil();
        return fu.saveUploadFile(file, "images/");
    }
}
