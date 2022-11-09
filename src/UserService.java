import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

public class UserService {
    public static final String FILE_NOT_FOUND = "Нет созданного файлы";
    public static final String FILE_CANT_BE_CREATED = "Не удалось создать файл!";
    private String filePath = "";
    private final LinkedList<User> userList = new LinkedList();
    private final LoggerController LOGGER = new LoggerController(Logger.getGlobal());
    private final Repository repository = new Repository();

    public boolean fileExist(){
        return repository.fileExist(filePath);
    }
    public boolean fileExist(String path){
        return repository.fileExist(path);
    }

    public void search() {
        throw new UnsupportedOperationException();
    }

    public void saveFileAs(String path) {
        repository.saveUserToFile(path, userList);
    }

    public void saveFile() {
        repository.saveUserToFile(filePath, userList);
    }

    public void removeUser() {
        throw new UnsupportedOperationException();
    }

    public void loadFile() {
        throw new UnsupportedOperationException();
    }

    public void newFile(String filePath) throws Exception {
        // if (fileExist(filePath)){
        //    return new ExecuteAnswer(false, "Не удалось создать файл, файл с таким имене уже существует.");
        // }

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
        }
        else{
            Exception e = new IllegalStateException(FILE_CANT_BE_CREATED);
            LOGGER.logIt(e);
        }
    }

    public void addUser(String fio, int age, String phone, String sex, String address) throws Exception {

        if (fileExist()){
            Exception e = new FileNotFoundException(FILE_NOT_FOUND);
            LOGGER.logIt(e);
            return;
        }

        try {
            userList.add(new User(fio, age, phone, Sex.valueOf(sex), address));
        }
        catch(Exception e){
            LOGGER.logIt(e.toString(), e);
        }
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

