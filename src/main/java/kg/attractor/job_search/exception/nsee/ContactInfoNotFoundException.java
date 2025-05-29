package kg.attractor.job_search.exception.nsee;

import java.util.NoSuchElementException;

public class ContactInfoNotFoundException extends NoSuchElementException {
    public ContactInfoNotFoundException() {}
    public ContactInfoNotFoundException(String message) {
        super(message);
    }
}
