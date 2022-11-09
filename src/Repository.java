import java.io.File;
import java.util.Collection;

public class Repository {
    public void saveUserToFile(String path, Collection<User> users){
        throw new UnsupportedOperationException();
    }

    public boolean fileExist(String path){
        return path.length() != 0 && new File(path).exists();
    }
}
