package kg.attractor.job_search.exception;

public class ContactInfoNotFoundException extends RuntimeException {
    public ContactInfoNotFoundException() {}
    public ContactInfoNotFoundException(String message) {
        super(message);
    }
}
