package kg.attractor.job_search.exception;

public class ApplicantNotFoundException extends RuntimeException {
    public ApplicantNotFoundException() {}
    public ApplicantNotFoundException(String message) {
        super(message);
    }
}
