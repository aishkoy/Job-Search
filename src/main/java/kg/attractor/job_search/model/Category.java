package kg.attractor.job_search.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Category {
    private Long id;
    private Long parentId;
    private String name;
}
