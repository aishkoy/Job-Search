package kg.attractor.job_search.exception;

import java.util.NoSuchElementException;

public class EducationInfoNotFoundException extends NoSuchElementException {
    public EducationInfoNotFoundException() {}
    public EducationInfoNotFoundException(String message) {
        super(message);
    }
}
