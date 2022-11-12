package repository;

import domain.Sex;
import domain.User;
import exceptions.RepositoryException;
import logic.CheckSumService;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileRepository {
    private final Logger LOGGER = Logger.getLogger(FileRepository.class.getName());
    private final CheckSumService checkSumService;

    public FileRepository(CheckSumService checkSumService) {
        this.checkSumService = checkSumService;
    }

    public boolean saveUserToFile(Collection<User> users, String path){
        try (FileWriter writer = new FileWriter(path, false)){
            writer.write(checkSumService.getUserListHash(users) + "\n");

            for (User user : users){
                writer.write(user.toString());
                writer.write("\r\n");
            }
            writer.flush();

            return true;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "failed to save file", e);
            throw new RepositoryException(e);
        }
    }

    public boolean fileExist(String path){
        return path.length() != 0 && new File(path).exists();
    }

    public List<User> loadFile(String path) {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))){
            // Read checksum
            Long controlCheckSum = Long.parseLong(reader.readLine().trim());

            // Read users
            User user = readUser(reader);
            while(user!=null){
                users.add(user);
                user = readUser(reader);
            }

            if (controlCheckSum.equals(checkSumService.getUserListHash(users)))
                return users;
        }
        catch (IOException e) {
            LOGGER.log(Level.WARNING, "failed to load file", e);
        }
        return users;
    }

    private User readUser(BufferedReader reader) {
        //Read name
        try {
            // user
            String name    = reader.readLine().trim();
            Integer age    = Integer.parseInt(reader.readLine().trim());
            String phone   = reader.readLine().trim();
            Sex sex        = Sex.valueOf(reader.readLine().trim().toUpperCase());
            String address = reader.readLine().trim();
            // separate line
            reader.readLine();
            return new User(name, age, phone, sex, address);
        }
        catch (IllegalArgumentException | IOException e){
            LOGGER.log(Level.WARNING, "failed to read user file", e);
            throw new RepositoryException(e);
        }
    }
}

