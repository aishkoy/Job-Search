package kg.attractor.job_search.exception.iae;

public class AlreadyRespondedException extends RuntimeException {
    public AlreadyRespondedException() {}
    public AlreadyRespondedException(String message) {
        super(message);
    }
}
