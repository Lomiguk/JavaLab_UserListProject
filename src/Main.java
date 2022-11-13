import logic.UserService;
import ui.ConsoleUI;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI(new UserService(new ArrayList<>()), System.in);
        consoleUI.start();
    }
}

