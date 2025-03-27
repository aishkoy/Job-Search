package kg.attractor.job_search.exception;

public class EmployerNotFoundException extends RuntimeException {
    public EmployerNotFoundException(){}
    public EmployerNotFoundException(String message) {
        super(message);
    }
}
