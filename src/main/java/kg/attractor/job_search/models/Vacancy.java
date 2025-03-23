package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

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
    private Timestamp createdDate;
    private Timestamp updateTime;
}
