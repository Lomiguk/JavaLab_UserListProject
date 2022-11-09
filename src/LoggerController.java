import java.util.logging.Logger;

public class LoggerController {
    private final int MAX_EXCEPTION_COUNT = 30;
    private final Logger LOGGER;
    private int exceptionCount = 0;

    public LoggerController(Logger LOGGER) {
        this.LOGGER = LOGGER;
    }

    public void logIt(String message, Exception e) {
        LOGGER.info(String.format("%s Exception: %s", message, e.toString()));
        exceptionCount++;

        if (exceptionCount > MAX_EXCEPTION_COUNT) {
            throw new RuntimeException("Превышен лимит исключений".concat(LOGGER.toString()));
        }
    }

    public void logIt(Exception e) {
        LOGGER.info(String.format("Exception: %s", e.toString()));
        exceptionCount++;

        if (exceptionCount > MAX_EXCEPTION_COUNT) {
            throw new RuntimeException("Превышен лимит исключений".concat(LOGGER.toString()));
        }
    }
}
