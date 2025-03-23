package kg.attractor.job_search.models;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Resume {
    private int id;
    private int applicantId;
    private String name;
    private int categoryId;
    private Float salary;
    private boolean isActive;
    private Timestamp createdData;
    private Timestamp updateTime;
}
