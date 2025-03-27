package kg.attractor.job_search.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){}
    public UserNotFoundException(String message) {
        super(message);
    }
}
