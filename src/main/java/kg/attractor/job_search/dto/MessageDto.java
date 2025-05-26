package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kg.attractor.job_search.dto.user.UserDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class MessageDto {
    Long id;

    @NotBlank
    String content;

    @NotNull
    @Builder.Default
    Timestamp timestamp = Timestamp.from(Instant.now());

    @NotNull
    ResponseDto response;

    @NotNull
    Boolean isRead;

    @NotNull
    UserDto user;
}
