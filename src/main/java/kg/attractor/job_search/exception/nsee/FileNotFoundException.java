package kg.attractor.job_search.exception.nsee;

import java.util.NoSuchElementException;

public class FileNotFoundException extends NoSuchElementException {
    public FileNotFoundException() {}
    public FileNotFoundException(String message) {
        super(message);
    }
}
