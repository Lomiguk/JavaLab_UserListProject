import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

public class Repository {
    private final LoggerController LOGGER = new LoggerController(Logger.getGlobal(), this.toString());
    private final CheckSumGenerator CHECKSUMMER = new CheckSumGenerator();
    public boolean saveUserToFile(Collection<User> users, String path){
        try (FileWriter writer = new FileWriter(path, false)){
            writer.write(CHECKSUMMER.getUserListHash(users) + "\n");

            for (User user : users){
                writer.write(user.toString());
                writer.write("\r\n");
            }

            //writer.write(users.toString());

            writer.flush();

            return true;
        } catch (IOException e) {
            LOGGER.logIt(e);
            return false;
        }
    }

    public boolean fileExist(String path){
        return path.length() != 0 && new File(path).exists();
    }

    public LinkedList<User> loadFile(String path) {
        try (FileReader fReader = new FileReader(path);
             BufferedReader reader = new BufferedReader(fReader)){
            // Read checksum
            Long controlCheckSum = Long.parseLong(reader.readLine().trim());

            LinkedList<User> users = new LinkedList<>();
            // Read users
            while (readUser(reader, users));

            if (controlCheckSum.equals(CHECKSUMMER.getUserListHash(users)))
                return users;
        }
        catch (IOException e) {
            LOGGER.logIt("loadFile", e);
        }


        return null;
    }

    private boolean readUser(BufferedReader reader, Collection<User> users) {
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

            users.add(new User(name, age, phone, sex, address));

            if (!reader.ready()) return false;
        }
        catch (IllegalArgumentException e){
            LOGGER.logIt("readUser", e);
            return false;
        }
        catch (IOException e) {
            //LOGGER.logIt("File is end", e);
            return false;
        }
        catch (Exception e){
            LOGGER.logIt(e);
            return false;
        }

        return true;
    }
}

