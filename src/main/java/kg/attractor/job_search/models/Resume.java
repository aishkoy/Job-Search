package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Resume {
    private int id;
    private int applicantId;
    private String name;
    private int categoryId;
    private Float salary;
    private boolean isActive;
    private LocalDateTime createdData;
    private LocalDateTime updateTime;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Resume{");
        sb.append("id=").append(id);
        sb.append(", applicantId=").append(applicantId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", categoryId=").append(categoryId);
        sb.append(", salary=").append(salary);
        sb.append(", isActive=").append(isActive);
        sb.append(", createdData=").append(createdData);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}
