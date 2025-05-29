package kg.attractor.job_search.exception.nsee;

import java.util.NoSuchElementException;

public class ResumeNotFoundException extends NoSuchElementException {
    public ResumeNotFoundException() {}
    public ResumeNotFoundException(String message) {
        super(message);
    }
}
