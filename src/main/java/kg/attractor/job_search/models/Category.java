package kg.attractor.job_search.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Category {
    private Long id;
    private Long parentId;
    private String name;
}
