package kg.attractor.job_search.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {}
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
