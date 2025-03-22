package kg.attractor.job_search.exceptions;

public class ResumeNotFoundException extends RuntimeException {
    public ResumeNotFoundException() {}
    public ResumeNotFoundException(String message) {
        super(message);
    }
}
