package kg.attractor.job_search.exceptions;

public class ContactInfoNotFoundException extends RuntimeException {
    public ContactInfoNotFoundException() {}
    public ContactInfoNotFoundException(String message) {
        super(message);
    }
}
