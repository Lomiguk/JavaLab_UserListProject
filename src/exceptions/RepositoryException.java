package exceptions;

public class RepositoryException extends RuntimeException {
    public RepositoryException(String cause) {
        super(cause);
    }
}
