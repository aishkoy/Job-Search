package kg.attractor.job_search.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ContactInfo {
    private Long id;
    private Long typeId;
    private Long resumeId;
    private String contactValue;
}
