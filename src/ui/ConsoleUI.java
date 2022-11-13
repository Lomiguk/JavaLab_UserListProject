package ui;

import api.Command;
import domain.Category;
import domain.Sex;
import domain.User;
import exceptions.IncorrectCheckSum;
import exceptions.IncorrectInputValueException;
import exceptions.NoneFileException;
import exceptions.RepositoryException;
import logic.UserService;
import utils.UIUtils;

import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConsoleUI {
    public static final int MIN_AGE = 14;
    private final UserService userService;
    private final Scanner scanner;
    private final Logger LOGGER = Logger.getLogger(ConsoleUI.class.getName());

    private int errorsCount = 0;
    private static final int MAX_ERRORS = 10;

    public ConsoleUI(UserService userService, InputStream inputStream) {
        this.userService = userService;
        this.scanner = new Scanner(inputStream);
    }

    public void start() {
        Command command = Command.START;
        while (command != Command.EXIT) {
            try {
                command = readCommand();
                runCommand(command);
            }
            catch (IncorrectCheckSum e){
                LOGGER.log(Level.WARNING, "Не удалось сверить контрольную сумму", e);
            }
            catch (IllegalArgumentException | NoSuchElementException | IllegalStateException e){
                System.out.println("Неверная команда!");
            }
            catch(IncorrectInputValueException e){
                LOGGER.log(Level.WARNING, "Неверное значение", e);
            }
            catch (NoneFileException e){
                LOGGER.log(Level.WARNING, "Файл не найден",e);
            }
            catch (RepositoryException e){
                LOGGER.log(Level.WARNING, "Не удалось считать файл",e);
            }
            catch (Exception e) {
                errorsCount++;
                LOGGER.log(Level.WARNING, "Вознкла ошибка:",e);
                if (errorsCount >= MAX_ERRORS) {
                    throw new RuntimeException(String.format("Произошло слишком много ошибок. Максимальное кол-во ошибок: %n",MAX_ERRORS));
                }
            }
        }
    }

    private Command readCommand(){
        System.out.printf("Введите команду (%s – %s)%n", Command.HELP, UIUtils.HELP_DESCRIPTION);
        System.out.print("> ");
        return Command.valueOf(scanner.nextLine().trim().toUpperCase());
    }

    private void runCommand(Command command) throws NoneFileException, IncorrectInputValueException, IncorrectCheckSum {
        switch (command) {
            case EXIT:
                break;
            case ADDUSER:
                addUser();
                break;
            case HELP:
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
    }

    private void search() {
        if (userService.fileNotExist()) return;

        System.out.println("На основе какого параметра вы желаете осуществить поиск?");
        String categories = Arrays.stream(Category.values())
                .map(it -> String.format("* %s (используйте команлу %s)", it.getKey(), it))
                .collect(Collectors.joining("\n"));
        System.out.println(categories);
        System.out.print("> ");

        Category category = Category.NAME;
        try {
            category = Category.valueOf(scanner.nextLine().trim().toUpperCase());
        } catch (NoSuchElementException | IllegalStateException e) {
            LOGGER.log(Level.WARNING, "", e);
        }

        String categoryStr = switch (category) {
            case NAME -> "Имя";
            case AGE -> "Возраст";
            case PHONE -> "Телефон";
            case SEX ->
                    String.format("Пол %s", String.join(" | ", Arrays.stream(Sex.values()).map(Object::toString).toArray(String[]::new)));
            case ADDRESS -> "Адресс";
        };

        System.out.printf("Введите %s%n", categoryStr);
        System.out.print("> ");

        List<User> searchedUsers = userService.search(category, scanner.nextLine().toUpperCase());

        if (searchedUsers == null || searchedUsers.size() == 0) {
            System.out.println("Не было найдено совпадений.");
        } else {
            for (Object user : searchedUsers) {
                System.out.println(user.toString());
                System.out.println();
            }
        }
    }

    private void removeUser() {
        if (userService.fileNotExist()) return;

        System.out.println("Введите имя пользователя, которого хотите удалить");
        System.out.print("> ");

        userService.removeUser(scanner.nextLine());
    }

    private void loadFile() throws IncorrectCheckSum, NoneFileException {
        System.out.println("Введите путь к файлу");
        System.out.print("> ");
        userService.loadFile(scanner.nextLine().trim());
        // Если exception'ы не сработали
        System.out.println("Файл успешно загружен");
    }

    private void saveFile() {
        if (userService.fileNotExist()) {
            System.out.println("Файл не был найден!");
            saveFileAs();
        }

        userService.saveFile();
        // Если exception'ы не сработали
        System.out.println("Файл успешно сохранён!");
    }

    private void saveFileAs() {
        System.out.println("Введите путь к файлу");
        System.out.print("> ");
        String path = scanner.nextLine().trim();

        if (!userService.fileExist(path)) return;
        if (!askUser("Желаете перезаписать файл?")) return;

        userService.saveFileAs(path);
        // Если exception'ы не сработали
        System.out.println("Файл успешно сохранён!");
    }

    private boolean askUser(String s) {
        System.out.println(s);
        System.out.println("1) Y - да");
        System.out.println("2) N - нет");

        return scanner.nextLine().equals("Y") || scanner.nextLine().equals("1");
    }

    private void help() {
        System.out.println();
        System.out.println(commandsMapToString());
        System.out.println();
    }

    private void addUser() throws NoneFileException, IncorrectInputValueException {
        if (userService.fileNotExist()) return;

        System.out.println("Введите имя");
        System.out.print("> ");
        String fio = scanner.nextLine().trim();
        System.out.println("Введите возраст");
        System.out.print("> ");
        Integer age;
        try{
            age = scanner.nextInt();
            if (age < MIN_AGE){
                throw new Exception();
            }
        }
        catch (Exception e){
            throw new IncorrectInputValueException("Невозможно сохранить как возраст");
        }
        scanner.nextLine();
        System.out.println("Введите номер телефона");
        System.out.print("> ");
        String phone = scanner.nextLine().trim();
        System.out.println("Выберите пол");
        String sexes = Arrays.stream(Sex.values())
                .map(it -> String.format("* %s (используйте команлу %s)", it.getKey(), it))
                .collect(Collectors.joining("\n"));
        System.out.println(sexes);
        System.out.print("> ");
        Sex sex;
        try{
            sex = Sex.valueOf(scanner.nextLine().toUpperCase().trim());
        }
        catch (Exception e){
            throw new IncorrectInputValueException("Невполучилось определить пол");
        }
        System.out.println("Введите адрес");
        System.out.print("> ");
        String address = scanner.nextLine().trim();

        if (userService.addUser(fio, age, phone, sex, address)) {
            System.out.println("Запись добавлена.");
        } else {
            System.out.println("Не удалось добавить запись.");
        }
    }

    private void newFile() {
        System.out.println("Введите путь к файлу целиком, или имя файла, который будет создан в том же каталоге, что и данная программа");
        System.out.print("> ");
        if (userService.newFile(scanner.nextLine().trim())) {
            System.out.println("Файл успешно создан.");
        } else {
            System.out.println("Не удалось создать файл.");
        }
    }

    private String commandsMapToString() {
        return UIUtils.COMMAND_TO_DESCRIPTION_MAP.entrySet()
                .stream()
                .map(commandStringEntry -> String.format("Используйте %s для %s", commandStringEntry.getKey(), commandStringEntry.getValue()))
                .collect(Collectors.joining(",\n"));
    }
}

