package persistence;

import model.Budget;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

// Represents a writer that writes JSON representation of budgets to file
// Code referenced from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs a writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of budgets to file
    public void write(List<Budget> budgets) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Budget budget : budgets) {
            jsonArray.put(budget.toJson());
        }
        json.put("budgets", jsonArray);
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
