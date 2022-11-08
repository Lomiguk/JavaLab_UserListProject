public class IncorrectInputValueException extends Exception {
    public IncorrectInputValueException() {
    }

    public IncorrectInputValueException(String message) {
        super(message);
    }

    public IncorrectInputValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectInputValueException(Throwable cause) {
        super(cause);
    }
}
