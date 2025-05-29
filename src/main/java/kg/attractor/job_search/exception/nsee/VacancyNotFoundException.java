package kg.attractor.job_search.exception.nsee;

import java.util.NoSuchElementException;

public class VacancyNotFoundException extends NoSuchElementException {
    public VacancyNotFoundException(String message) {
        super(message);
    }
}
