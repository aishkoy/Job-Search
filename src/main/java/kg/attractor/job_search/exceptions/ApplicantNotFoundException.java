package kg.attractor.job_search.exceptions;

public class ApplicantNotFoundException extends RuntimeException {
    public ApplicantNotFoundException() {}
    public ApplicantNotFoundException(String message) {
        super(message);
    }
}
