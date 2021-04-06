package persistence;

import model.*;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Represents a writer that writes a JSON representation of accounts to file.
 * Code referenced from:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonWriter {
    private static final int TAB = 4;
    private String destination;
    private JSONObject jsonObject;
    private PrintWriter writer;

    /**
     * Constructs a new writer to write to the specified destination file.
     *
     * @param destination the destination file
     */
    public JsonWriter(String destination) {
        this.destination = destination;
        jsonObject = new JSONObject();
    }

    /**
     * Opens the writer.
     *
     * @throws IOException if file cannot be opened for writing
     */
    public void open() throws IOException {
        try {
            String jsonData = readFile(destination);
            jsonObject = new JSONObject(jsonData);
        } catch (Exception exception) {
            jsonObject = new JSONObject();
        }
        writer = new PrintWriter(new File(destination));
    }

    /**
     * Writes a JSON representation of the specified account to file.
     *
     * @param account the account to be written to file
     */
    public void write(Account account) {
        jsonObject.put(account.getUsername(), account.toJson());
        saveToFile(jsonObject.toString(TAB));
    }

    /**
     * Reads the specified source file as string.
     *
     * @param source the source file
     * @return the source file as string
     * @throws IOException if an error occurs reading data from file
     */
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    /**
     * Closes the writer.
     */
    public void close() {
        writer.close();
    }

    /**
     * Writes the specified JSON string to file.
     *
     * @param jsonString the JSON string
     */
    private void saveToFile(String jsonString) {
        writer.print(jsonString);
    }
}
