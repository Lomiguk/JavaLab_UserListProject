public class Main {
    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI(new UserService());
        consoleUI.start();
    }
}

