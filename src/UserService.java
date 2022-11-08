import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

public class UserService {
    private String filePath = "";
    private LinkedList<User> userList = new LinkedList();
    private final LoggerController LOGGER = new LoggerController(Logger.getGlobal());

    public boolean fileExist(){
        return filePath.length() != 0 && new File(filePath).exists();
    }
    public boolean fileExist(String path){
        return path.length() != 0 && new File(path).exists();
    }

    public ExecuteAnswer search() {
        throw new UnsupportedOperationException();
    }

    public ExecuteAnswer saveFileAs() {
        throw new UnsupportedOperationException();
    }

    public ExecuteAnswer saveFile() {
        throw new UnsupportedOperationException();
    }

    public ExecuteAnswer removeUser() {
        throw new UnsupportedOperationException();
    }

    public ExecuteAnswer loadFile() {
        throw new UnsupportedOperationException();
    }

    public ExecuteAnswer newFile(String filePath) {
        if (fileExist(filePath)){
            return new ExecuteAnswer(false, "Не удалось создать файл, файл с таким имене уже существует.");
        }

        File file = new File(filePath);

        boolean isFileCreated = false;

        try{
            isFileCreated = file.createNewFile();
        }
        catch (IOException | SecurityException e) {
            LOGGER.logIt(e.toString(), e);
        }

        if(isFileCreated){
            this.filePath = filePath;
            return new ExecuteAnswer(true, "Файл успешно создан.");
        }
        else{
            return new ExecuteAnswer(false, "Не удалось создать файл.");
        }
    }

    public ExecuteAnswer addUser(String fio, int age, String phone, String sex, String address) {

        if (!fileExist()){
            return new ExecuteAnswer(false, "Нет открытого файла");
        }

        try {
            userList.add(new User(fio, age, phone, Sex.valueOf(sex), address));
        }
        catch(Exception e){
            LOGGER.logIt(e.toString(), e);
            return new ExecuteAnswer(false, "Не получилось добавить запись.");
        }

        return new ExecuteAnswer(true, "Запись успешно добавлена.");
    }

    public Integer tryReadAge(String ageStr) {
        Integer ageInt = null;
        try {
            ageInt = Integer.parseInt(ageStr);

            if (ageInt < 0) throw new IncorrectInputValueException("Возраст не может быть меньше 0");
        }
        catch (NumberFormatException e){
            LOGGER.logIt("This value can't be converted to integer.", e);
            return null;
        }
        catch (IncorrectInputValueException e) {
            LOGGER.logIt("Can't be age.", e);
            return null;
        }

        return ageInt;
    }
}

class LoggerController{
    private final Logger LOGGER;
    private int exceptionCount = 0;

    public LoggerController(Logger LOGGER) {
        this.LOGGER = LOGGER;
    }
    public void logIt(String message, Exception e){
        // idea сказала так сделать
        int MAX_EXCEPTION_COUNT = 30;

        LOGGER.info(String.format("%s Exception: %s", message, e.toString()));
        exceptionCount++;

        if (exceptionCount > MAX_EXCEPTION_COUNT){
            throw new RuntimeException("Превышен лимит исключений".concat(LOGGER.toString()));
        }
    }
}
