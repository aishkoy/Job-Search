package kg.attractor.job_search.exceptions.notFound;

public class EmployerNotFoundException extends RuntimeException {
    public EmployerNotFoundException(){}
    public EmployerNotFoundException(String message) {
        super(message);
    }
}
