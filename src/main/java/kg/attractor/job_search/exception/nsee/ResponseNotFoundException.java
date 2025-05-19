package kg.attractor.job_search.exception.nsee;

import java.util.NoSuchElementException;

public class ResponseNotFoundException extends NoSuchElementException {
    public ResponseNotFoundException() {}
    public ResponseNotFoundException(String message) {
        super(message);
    }
}
