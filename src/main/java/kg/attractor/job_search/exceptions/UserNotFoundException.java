package kg.attractor.job_search.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){}
    public UserNotFoundException(String message) {
        super(message);
    }
}
