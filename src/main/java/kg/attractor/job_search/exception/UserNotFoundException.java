package kg.attractor.job_search.exception;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
    public UserNotFoundException(){}
    public UserNotFoundException(String message) {
        super(message);
    }
}
