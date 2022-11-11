import java.io.*;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class CheckSumGenerator {
    LoggerController LOGGER = new LoggerController(Logger.getGlobal(), this.toString());
    private long getCrc32Checksum(byte[] bytes){
        Checksum crc32 = new CRC32();
        crc32.update(bytes,0, bytes.length);
        return crc32.getValue();
    }

    public Long getUserListHash(Collection<User> collection) throws IOException {
        return getCrc32Checksum(collection.stream().map(User::toString).sorted().collect(Collectors.joining()).getBytes());
    }
}
