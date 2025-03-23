package kg.attractor.job_search.exceptions;

public class EmployerNotFoundException extends Exception {
    public EmployerNotFoundException(){}
    public EmployerNotFoundException(String message) {
        super(message);
    }
}
