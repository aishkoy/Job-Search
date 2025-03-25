package kg.attractor.job_search.exceptions;

public class EducationInfoNotFoundException extends RuntimeException {
    public EducationInfoNotFoundException() {}
    public EducationInfoNotFoundException(String message) {
        super(message);
    }
}
