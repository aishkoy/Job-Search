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
        //297f4ebe-c153-4359-abdd-b6841a5a340c_avatar_monn_volf.jpg

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
    public ResponseEntity<?> getOutputFile(String filename, String subDir, MediaType mediaType) {
        Path filePath = Paths.get(UPLOAD_DIR + subDir + filename);

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
