package persistence;

import model.*;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Represents a Writer that writes a JSON representation of accounts to the destination file.
 * Code referenced from:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonWriter {
    private static final int TAB = 4;
    private String destination;
    private JSONObject jsonObject;
    private PrintWriter writer;

    /**
     * Creates a Writer to write to the destination file.
     *
     * @param destination the destination file
     */
    public JsonWriter(String destination) {
        this.destination = destination;
        jsonObject = new JSONObject();
    }

    /**
     * Opens the Writer.
     *
     * @throws IOException if the destination file cannot be opened for writing
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
     * Writes a JSON representation of the given Account to the destination file.
     *
     * @param account the Account to be written
     */
    public void write(Account account) {
        jsonObject.put(account.getUsername(), account.toJson());
        saveToFile(jsonObject.toString(TAB));
    }

    /**
     * Reads the source file as string.
     *
     * @param source the source file
     * @return the source file as string
     * @throws IOException if an error occurs reading data from the file
     */
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    /**
     * Closes the Writer.
     */
    public void close() {
        writer.close();
    }

    /**
     * Writes the JSON string to the destination file.
     * @param json the JSON string
     */
    private void saveToFile(String json) {
        writer.print(json);
    }
}
