package kg.attractor.job_search.exception.nsee;

import java.util.NoSuchElementException;

public class CategoryNotFoundException extends NoSuchElementException {
    public CategoryNotFoundException() {}
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
