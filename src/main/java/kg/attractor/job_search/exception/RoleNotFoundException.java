package kg.attractor.job_search.exception;

import java.util.NoSuchElementException;

public class RoleNotFoundException extends NoSuchElementException {
    public RoleNotFoundException() {}
    public RoleNotFoundException(String message) {
        super(message);
    }
}
