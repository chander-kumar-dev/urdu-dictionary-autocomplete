import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DictionaryLoader {
    public static final ArrayList<String> Dictionary = new ArrayList<>();

    private final Path filePath;

    public DictionaryLoader(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public ArrayList<String> dict() {
        Dictionary.clear();

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String poetName;

            while ((poetName = reader.readLine()) != null) {
                poetName = poetName.trim();

                if (!poetName.isEmpty()) {
                    Dictionary.add(poetName);
                }
            }

        } catch (IOException ex) {
            System.err.println("Could not read dictionary file: " + ex.getMessage());
        }

        return Dictionary;
    }
}
