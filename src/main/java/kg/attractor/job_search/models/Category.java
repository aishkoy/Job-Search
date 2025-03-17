package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class Category {
    private int id;
    private int parentId;
    private String name;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Category{");
        sb.append("id=").append(id);
        sb.append(", parentId=").append(parentId);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
