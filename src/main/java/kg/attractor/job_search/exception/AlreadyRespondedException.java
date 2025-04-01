package kg.attractor.job_search.exception;

public class AlreadyRespondedException extends RuntimeException {
    public AlreadyRespondedException() {}
    public AlreadyRespondedException(String message) {
        super(message);
    }
}
