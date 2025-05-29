package kg.attractor.job_search.exception.nsee;

import java.util.NoSuchElementException;

public class ApplicantNotFoundException extends NoSuchElementException {
    public ApplicantNotFoundException() {}
    public ApplicantNotFoundException(String message) {
        super(message);
    }
}
