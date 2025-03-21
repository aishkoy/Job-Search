package kg.attractor.job_search.exceptions.id;

public class IncorrectUserIdException extends RuntimeException {
    public IncorrectUserIdException() {}
    public IncorrectUserIdException(String message) {
        super(message);
    }
}
