package kg.attractor.job_search.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ContactInfoDto {
    private Long id;
    private Long typeId;
    private Long resumeId;
    private String value;
}

