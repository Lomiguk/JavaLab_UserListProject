import java.util.LinkedList;

public class ExecuteAnswer {
    private boolean IsExecutable;
    private String Message;
    public boolean isExit;

    public boolean isExecutable() {
        return IsExecutable;
    }

    public String getMessage() {
        return Message;
    }

    public ExecuteAnswer() {
        isExit = false;
    }

    public ExecuteAnswer(boolean isExecutable, String message) {
        this.IsExecutable = isExecutable;
        this.Message = message;
        this.isExit = false;
    }

    public ExecuteAnswer(boolean isExecutable, String message, boolean isExit) {
        this.IsExecutable = isExecutable;
        this.Message = message;
        this.isExit = isExit;
    }
}
