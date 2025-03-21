package kg.attractor.job_search.exceptions.id;

public class IncorrectVacancyIdException extends RuntimeException {
    public IncorrectVacancyIdException() {}
    public IncorrectVacancyIdException(String message) {
        super(message);
    }
}
