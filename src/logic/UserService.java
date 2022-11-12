package logic;

import domain.Category;
import domain.Sex;
import domain.User;
import exceptions.IncorrectInputValueException;
import exceptions.NoneFileException;
import repository.FileRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private String filePath = "";

    private List<User> userList;
    private final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private final FileRepository repository = new FileRepository(new CheckSumService());

    public boolean fileNotExist(){
        return !repository.fileExist(filePath);
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
            LOGGER.log(Level.WARNING, "Failed to read sex", e);
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
            LOGGER.log(Level.WARNING, "Failed to read age", e);
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
        userList.removeIf(user -> user.getFIO().equalsIgnoreCase(s));
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

    public boolean newFile(String filePath){
        File file = new File(filePath);

        boolean isFileCreated = false;

        try{
            isFileCreated = file.createNewFile();
        }
        catch (IOException | SecurityException e) {
        }

        if(isFileCreated){
            this.filePath = filePath;
        }

        return isFileCreated;
    }

    public boolean addUser(String fio, int age, String phone, String sex, String address) throws NoneFileException {
        if (fileNotExist()){
            throw new NoneFileException("Unsuccessful file create");
        }

        return userList.add(new User(fio, age, phone, Sex.valueOf(sex), address));
    }

    public Integer tryReadAge(String ageStr) throws IncorrectInputValueException {
        int ageInt;
        try {
            ageInt = Integer.parseInt(ageStr);

            if (ageInt < 0) throw new IncorrectInputValueException("Can't be lower then 0");
        }
        catch (NumberFormatException e){
            LOGGER.log(Level.WARNING, "Failed to format string to age", e);
            return null;
        }

        return ageInt;
    }
}

