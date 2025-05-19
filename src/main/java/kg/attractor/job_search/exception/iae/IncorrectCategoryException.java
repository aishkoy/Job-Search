package kg.attractor.job_search.exception.iae;

public class IncorrectCategoryException extends IllegalArgumentException {
    public IncorrectCategoryException(){}
    public IncorrectCategoryException(String message) {
        super(message);
    }
}
