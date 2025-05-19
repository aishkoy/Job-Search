package kg.attractor.job_search.exception.nsee;

import java.util.NoSuchElementException;

public class EmployerNotFoundException extends NoSuchElementException {
    public EmployerNotFoundException(){}
    public EmployerNotFoundException(String message) {
        super(message);
    }
}
