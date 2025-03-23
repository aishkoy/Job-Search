package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class Category {
    private int id;
    private int parentId;
    private String name;
}
