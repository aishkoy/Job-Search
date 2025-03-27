package kg.attractor.job_search.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {}
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
