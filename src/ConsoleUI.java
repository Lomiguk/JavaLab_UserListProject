import java.util.*;
import java.util.stream.Collectors;

public class ConsoleUI {
    private final UserService userService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(UserService userService) {
        this.userService = userService;
    }

    public void start(){
        while (printMenu());
    }

    public boolean printMenu(){
        System.out.printf("Введите команду (%s – %s)%n", Command.HELP ,UIUtils.HELP_DESCRIPTION);
        System.out.print("> ");
        ExecuteAnswer executeAnswer = execute(Command.valueOf(scanner.nextLine()));

        if (!executeAnswer.isExecutable()){
            System.out.println(executeAnswer.getMessage());
            System.out.println();
        }

        return !executeAnswer.isExit;
    }

    private ExecuteAnswer execute(Command command) {

        switch (command){
            case EXIT:
                return new ExecuteAnswer(true, "Выполнена команда EXIT", true);
            case ADDUSER:
                ExecuteAnswer executeAnswer = addUser();
                return executeAnswer;
            case HELP :
                help();
                return new ExecuteAnswer(true, "Выполнена команда HELP");
            case NEWFILE:
                return newFile();
                /*
            case LOADFILE:
                loadFile();
                return;
            case REMOVEUSER:
                removeUser();
                return;
            case SAVEFILE:
                saveFile();
                return;
            case SAVEFILEAS:
                saveFileAs();
                return;
            case SEARCH:
                search();
                return;

                 */
            default:
                throw new IllegalStateException("Unexpected value: " + command);
        }
    }

    private void help(){
        System.out.println();
        System.out.println(commandsMapToString());
        System.out.println();
    }

    private ExecuteAnswer addUser(){
        if (!userService.fileExist()){
            return new ExecuteAnswer(false, "Нет открытого файла");
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

        return userService.addUser(fio, age, phone, sex, address);
    }

    private ExecuteAnswer newFile(){
        System.out.println("Введите путь к файлу целиком, или имя файла, который будет создан в том же каталоге, что и данная программа");
        System.out.print("> ");
        return userService.newFile(scanner.nextLine());
    }

    private String commandsMapToString(){
        return UIUtils.COMMAND_TO_DESCRIPTION_MAP.entrySet()
                .stream()
                .map(commandStringEntry -> String.format("Используйте %s для %s", commandStringEntry.getKey(), commandStringEntry.getValue()))
                .collect(Collectors.joining(",\n"));
    }
}

