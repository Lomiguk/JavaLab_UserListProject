import logic.UserService;
import ui.ConsoleUI;

public class Main {
    public static void main(String[] args) throws Exception {
        ConsoleUI consoleUI = new ConsoleUI(new UserService());
        consoleUI.start();
    }
}

