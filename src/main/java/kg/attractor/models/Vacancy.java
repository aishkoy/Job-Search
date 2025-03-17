package kg.attractor.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Vacancy {
    private int id;
    private String name;
    private String description;
    private int categoryId;
    private Float salary;
    private int expFrom;
    private int expTo;
    private boolean isActive;
    private int authorId;
    private LocalDateTime createdDate;
    private LocalDateTime updateTime;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Vacancy{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", categoryId=").append(categoryId);
        sb.append(", salary=").append(salary);
        sb.append(", expFrom=").append(expFrom);
        sb.append(", expTo=").append(expTo);
        sb.append(", isActive=").append(isActive);
        sb.append(", authorId=").append(authorId);
        sb.append(", createdDate=").append(createdDate);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}
