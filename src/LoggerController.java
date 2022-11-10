import java.util.logging.Logger;

public class LoggerController {
    private final int MAX_EXCEPTION_COUNT = 30;
    private final Logger LOGGER;
    private final String ParentName;
    private int exceptionCount = 0;

    public LoggerController(Logger LOGGER, String parentName) {
        this.LOGGER = LOGGER;
        this.ParentName = parentName;
    }

    public void logIt(String message, Exception e) {
        LOGGER.info(String.format("%s Parent: %s\n Exception: %s", message, this.ParentName, e.toString()));
        exceptionCount++;

        if (exceptionCount > MAX_EXCEPTION_COUNT) {
            throw new RuntimeException("Превышен лимит исключений".concat(LOGGER.toString()));
        }
    }

    public void logIt(Exception e) {
        LOGGER.info(String.format("Parent: %s \n Exception: %s", this.ParentName, e.toString()));
        exceptionCount++;

        if (exceptionCount > MAX_EXCEPTION_COUNT) {
            throw new RuntimeException("Превышен лимит исключений".concat(LOGGER.toString()));
        }
    }
}
