package kg.attractor.job_search.exception.nsee;

import java.util.NoSuchElementException;

public class MessageNotFoundException extends NoSuchElementException {
    public MessageNotFoundException(String message) {
        super(message);
    }
}
