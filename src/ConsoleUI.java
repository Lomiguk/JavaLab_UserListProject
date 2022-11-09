import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConsoleUI {
    private final UserService userService;
    private final Scanner scanner = new Scanner(System.in);
    private final LoggerController LOGGER = new LoggerController(Logger.getGlobal());

    public ConsoleUI(UserService userService) {
        this.userService = userService;
    }

    public void start() throws Exception {
        while (printMenu());
    }

    public boolean printMenu() throws Exception {
        System.out.printf("Введите команду (%s – %s)%n", Command.HELP ,UIUtils.HELP_DESCRIPTION);
        System.out.print("> ");

        Command command;
        try {
            command = Command.valueOf(scanner.nextLine().toUpperCase());
        }
        catch(NoSuchElementException | IllegalStateException e){
            LOGGER.logIt("Scanner exception", e);
            return true;
        }
        catch (IllegalArgumentException e){
            LOGGER.logIt(e);
            return true;
        }

        return execute(command);
    }

    private boolean execute(Command command) throws Exception {

        switch (command){
            case EXIT:
                return false;
            case ADDUSER:
                addUser();
                break;
            case HELP :
                help();
                break;
            case NEWFILE:
                newFile();
                break;
            case SAVEFILEAS:
                saveFileAs();
                break;
            case SAVEFILE:
                saveFile();
                break;
                /*
            case LOADFILE:
                loadFile();
                return;
            case REMOVEUSER:
                removeUser();
                return;
            case SEARCH:
                search();
                return;

                 */
            default:
                throw new IllegalStateException("Unexpected value: " + command);
        }

        return true;
    }

    private void saveFile() {
        if (userService.fileExist()) {
            System.out.println("Файл не был найден!");
            saveFileAs();
        }

        userService.saveFile();
    }

    private void saveFileAs() {
        System.out.println("Введите путь к файлу");
        System.out.println("> ");
        String path = scanner.nextLine();

        if (userService.fileExist(path)){
            System.out.println("Такой файл уже существует!");
            if (askUser("Желаете перезаписать файл?")){
                userService.saveFileAs(path);
            }
        }
    }

    private boolean askUser(String s) {
        System.out.println(s);
        System.out.println("1) Y - да");
        System.out.println("2) N - нет");

        return scanner.nextLine().equals("Y") || scanner.nextLine().equals("1");
    }

    private void help(){
        System.out.println();
        System.out.println(commandsMapToString());
        System.out.println();
    }

    private void addUser() throws Exception {
        if (userService.fileExist()){
            Exception e = new IllegalStateException("Нет открытого файла");
            LOGGER.logIt(e);
            return;
        }

        System.out.println("Введите имя");
        System.out.print("> ");
        String fio = scanner.nextLine();

        System.out.println("Введите возраст");
        Integer age = null;
        while (age == null){
            System.out.print("> ");
            age = userService.tryReadAge(scanner.nextLine());
        }

        System.out.println("Введите номер телефона");
        System.out.print("> ");
        String phone = scanner.nextLine();

        System.out.println("Выберите пол");
        System.out.println("1) MALE");
        System.out.println("2) FEMALE");
        System.out.print("> ");
        String sex = scanner.nextLine();

        System.out.println("Введите адрес");
        System.out.print("> ");
        String address = scanner.nextLine();

        userService.addUser(fio, age, phone, sex, address);
    }

    private void newFile() throws Exception {
        System.out.println("Введите путь к файлу целиком, или имя файла, который будет создан в том же каталоге, что и данная программа");
        System.out.print("> ");
        userService.newFile(scanner.nextLine());
    }

    private String commandsMapToString(){
        return UIUtils.COMMAND_TO_DESCRIPTION_MAP.entrySet()
                .stream()
                .map(commandStringEntry -> String.format("Используйте %s для %s", commandStringEntry.getKey(), commandStringEntry.getValue()))
                .collect(Collectors.joining(",\n"));
    }
}
