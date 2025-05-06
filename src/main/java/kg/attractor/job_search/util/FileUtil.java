package kg.attractor.job_search.util;

import kg.attractor.job_search.exception.FileNotFoundException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@UtilityClass
public class FileUtil {

    private static final String UPLOAD_DIR = "data/";

    @SneakyThrows
    public String saveUploadFile(MultipartFile file, String subDir) {
        String uuidFile = UUID.randomUUID().toString();
        String resultFileName = uuidFile + "_" + file.getOriginalFilename();

        Path pathDir = Paths.get(UPLOAD_DIR + subDir);
        Files.createDirectories(pathDir);

        Path filePath = Paths.get(pathDir + "/" + resultFileName);
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
        try (OutputStream os = Files.newOutputStream(filePath)) {
            os.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultFileName;
    }

    @SneakyThrows
    public ResponseEntity<?> getStaticFile(String fileName, String subDir, MediaType mediaType) {
        if (!subDir.endsWith("/") && !subDir.isEmpty()) {
            subDir += "/";
        }

        Path filePath = Paths.get("src/main/resources/static/" + subDir + fileName);

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Дефолтный аватар не найден: " + subDir + fileName);
        }

        byte[] image = Files.readAllBytes(filePath);
        Resource resource = new ByteArrayResource(image);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + fileName + "\"")
                .contentLength(resource.contentLength())
                .contentType(mediaType)
                .body(resource);
    }

    @SneakyThrows
    public ResponseEntity<?> getOutputFile(String filename, String subDir, MediaType mediaType) {
        if (!subDir.endsWith("/") && !subDir.isEmpty()) {
            subDir += "/";
        }

        Path filePath = Paths.get(UPLOAD_DIR + subDir + filename);

        if (Files.isDirectory(filePath)) {
            throw new FileNotFoundException("Указанный путь является директорией, а не файлом: " + filePath);
        }

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Файл не найден: " + filename);
        }

        byte[] image = Files.readAllBytes(filePath);
        Resource resource = new ByteArrayResource(image);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + filename + "\"")
                .contentLength(resource.contentLength())
                .contentType(mediaType)
                .body(resource);
    }
}
