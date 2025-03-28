package kg.attractor.job_search.exception;

import java.util.NoSuchElementException;

public class ContactTypeNotFoundException extends NoSuchElementException {
    public ContactTypeNotFoundException(String message) {
        super(message);
    }
}
