package kg.attractor.job_search.exceptions;

public class IncorrectDateException extends RuntimeException {
    public IncorrectDateException(){}
    public IncorrectDateException(String message) {
        super(message);
    }
}
