package logic;

import domain.Category;
import domain.Sex;
import domain.User;
import exceptions.IncorrectCheckSum;
import exceptions.IncorrectInputValueException;
import exceptions.NoneFileException;
import repository.FileRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UserService {
    private String filePath = "";

    private List<User> userList;
    private final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private final FileRepository repository = new FileRepository(new CheckSumService());

    public UserService(List<User> userList) {
        this.userList = userList;
    }

    public boolean fileNotExist(){
        return !repository.fileExist(filePath);
    }
    public boolean fileExist(String path){
        return repository.fileExist(path);
    }

    public List<User> search(Category category, String s) {
        return switch (category){
            case NAME -> searchByName(s);
            case AGE -> searchByAge(s);
            case SEX -> searchBySex(s);
            case PHONE -> searchByPhone(s);
            case ADDRESS -> searchByAddress(s);
        };
    }

    private List<User> searchByAddress(String inputAddress) {
        return userList.stream()
                .filter(user -> user.getAddress().equalsIgnoreCase(inputAddress.trim()))
                .collect(Collectors.toList());
    }

    private List<User> searchByPhone(String inputPhone) {
        return userList.stream()
                .filter(user -> user.getPhone().equalsIgnoreCase(inputPhone.trim()))
                .toList();
    }

    private List<User> searchBySex(String stringSex) {
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
                .toList();
    }

    private List<User> searchByAge(String stringAge) {
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
                .toList();
    }

    private List<User> searchByName(String name) {
        return userList.stream()
                .filter(user -> user.getFIO().equalsIgnoreCase(name.trim()))
                .toList();
    }

    public void saveFileAs(String path) {
        repository.saveUserToFile(userList,path);
    }

    public void saveFile() {
        repository.saveUserToFile(userList, filePath);
    }

    public void removeUser(String s) {
        userList.removeIf(user -> user.getFIO().equalsIgnoreCase(s));
    }

    public void loadFile(String path) throws IncorrectCheckSum, NoneFileException {
        userList = repository.loadFile(path);
        this.filePath = path;
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

    public boolean addUser(String fio, int age, String phone, Sex sex, String address) throws NoneFileException {
        if (fileNotExist()){
            throw new NoneFileException("Unsuccessful file create");
        }

        return userList.add(new User(fio, age, phone, sex, address));
    }
}

