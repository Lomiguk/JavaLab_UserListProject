import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConsoleUI {
    private final UserService userService;
    private final Scanner scanner = new Scanner(System.in);
    private final LoggerController LOGGER = new LoggerController(Logger.getGlobal(), this.toString());

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
            command = Command.valueOf(scanner.nextLine().trim().toUpperCase());
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
            case LOADFILE:
                loadFile();
                break;
            case REMOVEUSER:
                removeUser();
                break;
            case SEARCH:
                search();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + command);
        }

        return true;
    }

    private void search() {
        if(checkFileNotExist()) return;

        System.out.println("На основе какого параметра вы желаете осуществить поиск?");
        System.out.println("1) Имя (используйте команду NAME)");
        System.out.println("2) Возраст (используйте команду AGE)");
        System.out.println("3) Телефон (используйте команду PHONE)");
        System.out.println("4) Пол (используйте команду SEX)");
        System.out.println("5) Адресс (используйте команду ADDRESS)");
        System.out.print("> ");

        Category category = Category.NAME;
        try {
            category = Category.valueOf(scanner.nextLine().trim().toUpperCase());
        }
        catch(NoSuchElementException | IllegalStateException e){
            LOGGER.logIt("Scanner exception", e);
        }
        catch (IllegalArgumentException e){
            LOGGER.logIt(e);
        }

        String categoryStr = switch (category) {
            case NAME -> "Имя";
            case AGE -> "Возраст";
            case PHONE -> "Телефон";
            case SEX -> String.format("Пол %s", String.join(" | ", Arrays.stream(Sex.values()).map(Object::toString).toArray(String[]::new)));
            case ADDRESS -> "Адресс";
        };

        System.out.printf("Введите %s%n", categoryStr);
        System.out.print("> ");

        Object[] searchedUsers = userService.search(category, scanner.nextLine().toUpperCase());

        if (searchedUsers == null || searchedUsers.length == 0){
            System.out.println("Не было найдено совпадений.");
        }
        else{
            for (Object user: searchedUsers) {
                System.out.println(user.toString());
                System.out.println();
            }
        }
    }

    private void removeUser() {
        if(checkFileNotExist()) return;

        System.out.println("Введите имя пользователя, которого хотите удалить");
        System.out.print("> ");

        userService.removeUser(scanner.nextLine());
    }

    private void loadFile() {
        System.out.println("Введите путь к файлу");
        System.out.print("> ");
        if (userService.loadFile(scanner.nextLine().trim())){
            System.out.println("Файл успешно загружен!");
        }
        else{
            System.out.println("Не удалось загрузить файл!");
        }
    }

    private void saveFile() {
        if (userService.fileNotExist()) {
            System.out.println("Файл не был найден!");
            saveFileAs();
        }

        if (userService.saveFile()){
            System.out.println("Файл успешно сохранён!");
        }
        else {
            System.out.println("Не удалось сохранить файл");
        }
    }

    private void saveFileAs() {
        System.out.println("Введите путь к файлу");
        System.out.print("> ");
        String path = scanner.nextLine().trim();

        if (!userService.fileExist(path)) return;
        if (!askUser("Желаете перезаписать файл?")) return;

        if (userService.saveFileAs(path)){
            System.out.println("Файл успешно сохранён!");
        }
        else {
            System.out.println("Не удалось сохранить файл");
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

    private void addUser(){
        if(checkFileNotExist()) return;

        System.out.println("Введите имя");
        System.out.print("> ");
        String fio = scanner.nextLine().trim();

        System.out.println("Введите возраст");
        Integer age = null;
        while (age == null){
            System.out.print("> ");
            age = userService.tryReadAge(scanner.nextLine().trim());
        }

        System.out.println("Введите номер телефона");
        System.out.print("> ");
        String phone = scanner.nextLine().trim();

        System.out.println("Выберите пол");
        System.out.println("1) MALE");
        System.out.println("2) FEMALE");
        System.out.print("> ");
        String sex = scanner.nextLine().toUpperCase().trim();

        System.out.println("Введите адрес");
        System.out.print("> ");
        String address = scanner.nextLine().trim();

        if (userService.addUser(fio, age, phone, sex, address)){
            System.out.println("Запись добавлена.");
        }
        else{
            System.out.println("Не удалось добавить запись.");
        }
    }

    private void newFile(){
        System.out.println("Введите путь к файлу целиком, или имя файла, который будет создан в том же каталоге, что и данная программа");
        System.out.print("> ");
        if(userService.newFile(scanner.nextLine().trim())){
            System.out.println("Файл успешно создан.");
        }
        else {
            System.out.println("Не удалось создать файл.");
        }
    }

    private String commandsMapToString(){
        return UIUtils.COMMAND_TO_DESCRIPTION_MAP.entrySet()
                .stream()
                .map(commandStringEntry -> String.format("Используйте %s для %s", commandStringEntry.getKey(), commandStringEntry.getValue()))
                .collect(Collectors.joining(",\n"));
    }

    private boolean checkFileNotExist(){
        if (userService.fileNotExist()){
            LOGGER.logIt(new IllegalStateException("Нет открытого файла"));
            return true;
        }
        return false;
    }
}

