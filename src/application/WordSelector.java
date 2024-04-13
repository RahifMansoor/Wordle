package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class WordSelector {
    private List<String> words;

    public WordSelector(String filename) {
        try {
            // Use Path.of() if using Java 11 or newer
            Path path = Paths.get(filename);
            if (Files.exists(path)) {
                words = Files.readAllLines(path);
            } else {
                throw new IOException("File not found: " + filename);
            }
        } catch (IOException e) {
            System.err.println("Failed to load words from file: " + filename);
            e.printStackTrace();
            words = List.of("dummy"); // Default word to prevent further null errors
        }
    }

    public String getRandomWord() {
        if (!words.isEmpty()) {
            Random random = new Random();
            return words.get(random.nextInt(words.size()));
        } else {
            System.err.println("Word list is empty.");
            return "dummy"; // Fallback word
        }
    }
}
