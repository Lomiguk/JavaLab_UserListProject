import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

public class Repository {
    private final LoggerController LOGGER = new LoggerController(Logger.getGlobal(), this.toString());
    private final CheckSumGenerator CHECKSUMMER = new CheckSumGenerator();
    public void saveUserToFile(String path, Collection<User> users){
        try (FileWriter writer = new FileWriter(path, false)){
            writer.write(CHECKSUMMER.getUserListHash(users) + "\n");

            for (User user : users){
                writer.write(user.toString().join("\n"));
            }

            writer.flush();
        } catch (IOException e) {
            LOGGER.logIt(e);
        }
    }

    public boolean fileExist(String path){
        return path.length() != 0 && new File(path).exists();
    }

    public LinkedList<User> loadFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))){
            Long checkSum = Long.parseLong(reader.readLine().trim());

            LinkedList<User> users = new LinkedList<User>();
            boolean isEnd = false;
            while(!isEnd){

            }

        } catch (IOException e) {
            LOGGER.logIt(e);
        }

        throw new UnsupportedOperationException();
    }
}

