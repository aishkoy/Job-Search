package kg.attractor.job_search.exception.nsee;

import java.util.NoSuchElementException;

public class WorkExperienceNotFoundException extends NoSuchElementException {
    public WorkExperienceNotFoundException(){}
    public WorkExperienceNotFoundException(String message) {
        super(message);
    }
}
