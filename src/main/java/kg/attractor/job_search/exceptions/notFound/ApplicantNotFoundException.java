package kg.attractor.job_search.exceptions.notFound;

public class ApplicantNotFoundException extends RuntimeException {
    public ApplicantNotFoundException() {}
    public ApplicantNotFoundException(String message) {
        super(message);
    }
}
