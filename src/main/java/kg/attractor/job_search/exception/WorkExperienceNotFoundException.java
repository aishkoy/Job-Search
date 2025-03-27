package kg.attractor.job_search.exception;

public class WorkExperienceNotFoundException extends RuntimeException {
    public WorkExperienceNotFoundException(){}
    public WorkExperienceNotFoundException(String message) {
        super(message);
    }
}
