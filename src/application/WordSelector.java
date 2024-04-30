package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordSelector {
    private Set<String> validWords;

    public WordSelector(String filename) {
        try {
            Path path = Paths.get(filename);
            if (Files.exists(path)) {
            	List<String> lines = Files.readAllLines(path);
            	validWords = new HashSet<>();
            	for (String line : lines) {
            	    validWords.add(line.trim().toUpperCase()); // Trim and convert to upper case
            	}
            } else {
                throw new IOException("File not found: " + filename);
            }
        } catch (IOException e) {
            System.err.println("Failed to load words from file: " + filename);
            e.printStackTrace();
            validWords = new HashSet<>(); 
            validWords.add("dummy"); // Add a default word to prevent further errors
        }
        System.out.println("Loaded " + validWords.size() + " valid words.");
    }

    public String getRandomWord() {
        if (!validWords.isEmpty()) {
            int index = new Random().nextInt(validWords.size());
            return (String) validWords.toArray()[index];
        } else {
            System.err.println("Word list is empty.");
            return "dummy"; // Fallback word
        }
    }

    public boolean isValidWord(String word) {
        return validWords.contains(word.toUpperCase().trim());
    }
}
