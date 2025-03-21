package kg.attractor.job_search.exceptions.notFound;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){}
    public UserNotFoundException(String message) {
        super(message);
    }
}
