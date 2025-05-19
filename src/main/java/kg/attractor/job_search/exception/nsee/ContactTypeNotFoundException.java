package kg.attractor.job_search.exception.nsee;

import java.util.NoSuchElementException;

public class ContactTypeNotFoundException extends NoSuchElementException {
    public ContactTypeNotFoundException(String message) {
        super(message);
    }
}
