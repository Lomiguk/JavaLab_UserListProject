import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

public class UserService {
    public static final String FILE_NOT_FOUND = "Нет созданного файлы";
    public static final String FILE_CANT_BE_CREATED = "Не удалось создать файл!";
    private String filePath = "";

    // How to make final??
    private LinkedList<User> userList = new LinkedList();
    private final LoggerController LOGGER = new LoggerController(Logger.getGlobal(), this.toString());
    private final Repository repository = new Repository();

    public boolean fileExist(){
        return repository.fileExist(filePath);
    }
    public boolean fileExist(String path){
        return repository.fileExist(path);
    }

    public Object[] search(Category category, String s) {
        return switch (category){
            case NAME -> searchByName(s);
            case AGE -> searchByAge(s);
            case SEX -> searchBySex(s);
            case PHONE -> searchByPhone(s);
            case ADDRESS -> searchByAddress(s);
        };
    }

    private Object[] searchByAddress(String inputAddress) {
        return userList.stream()
                .filter(user -> user.getAddress().equalsIgnoreCase(inputAddress.trim()))
                .toArray();
    }

    private Object[] searchByPhone(String inputPhone) {
        return userList.stream()
                .filter(user -> user.getPhone().equalsIgnoreCase(inputPhone.trim()))
                .toArray();
    }

    private Object[] searchBySex(String stringSex) {
        Sex sex;
        try{
            sex = Sex.valueOf(stringSex.toUpperCase().trim());
        }
        catch(Exception e){
            LOGGER.logIt(e);
            return null;
        }

        return userList.stream()
                .filter(user -> user.getSex().equals(sex))
                .toArray();
    }

    private Object[] searchByAge(String stringAge) {
        int age;
        try {
            age = Integer.parseInt(stringAge.trim());
        }
        catch (NumberFormatException e){
            LOGGER.logIt(e);
            return null;
        }

        return userList.stream()
                .filter(user -> user.getAge().equals(age))
                .toArray();
    }

    private Object[] searchByName(String name) {
        return userList.stream()
                .filter(user -> user.getFIO().equalsIgnoreCase(name.trim()))
                .toArray();
    }

    public boolean saveFileAs(String path) {
        return repository.saveUserToFile(userList,path);
    }

    public boolean saveFile() {
        return repository.saveUserToFile(userList, filePath);
    }

    public void removeUser(String s) {
        throw new UnsupportedOperationException();
    }

    public boolean loadFile(String path) {
        userList = repository.loadFile(path);
        if (userList != null) {
            this.filePath = path;
            return true;
        }
        else {
            this.filePath = "";
            return false;
        }
    }

    public boolean newFile(String filePath) throws Exception {
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

        return isFileCreated;
    }

    public boolean addUser(String fio, int age, String phone, String sex, String address) throws Exception {

        if (!fileExist()){
            Exception e = new FileNotFoundException(FILE_NOT_FOUND);
            LOGGER.logIt(e);
            return false;
        }

        try {
            return userList.add(new User(fio, age, phone, Sex.valueOf(sex), address));
        }
        catch(Exception e){
            LOGGER.logIt(e.toString(), e);
        }
        return false;
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

