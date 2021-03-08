package persistence;

import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads account from JSON data stored in file
// Code referenced from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs a reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads account from file and returns budgets;
    // throws IOException if an error occurs reading data from file
    public Account read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAccount(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses account from JSON object and returns it
    private Account parseAccount(JSONObject jsonObject) {
        String username = jsonObject.getString("account username");
        String password = jsonObject.getString("account password");
        Account account = new Account(username, password);
        addBudgets(account, jsonObject);
        return account;
    }

    // MODIFIES: budgets
    // EFFECTS: parses budgets from JSON object and adds them to list of budgets
    private void addBudgets(Account account, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("budgets");
        for (Object json : jsonArray) {
            JSONObject nextBudget = (JSONObject) json;
            addBudget(account, nextBudget);
        }
    }

    // MODIFIES: budgets
    // EFFECTS: parses budget from JSON object and adds it to list of budgets
    private void addBudget(Account account, JSONObject jsonObject) {
        String name = jsonObject.getString("budget name");
        BigDecimal amount = jsonObject.getBigDecimal("budget amount");
        Budget budget = new Budget(name, amount);
        addCategories(budget, jsonObject);
        account.addBudget(budget);
    }

    // MODIFIES: budget
    // EFFECTS: parses categories from JSON object and adds them to budget
    private void addCategories(Budget budget, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("budget categories");
        for (Object json : jsonArray) {
            JSONObject category = (JSONObject) json;
            addCategory(budget, category);
        }
    }

    // MODIFIES: budget
    // EFFECTS: parses category from JSON object and adds it to budget
    private void addCategory(Budget budget, JSONObject jsonObject) {
        String categoryName = jsonObject.getString("category name");
        Category category = new Category(categoryName);
        addTransactions(category, jsonObject);
        budget.addCategory(category);
    }

    // MODIFIES: budget, category
    // EFFECTS: parses transactions from JSON object and adds them to category
    private void addTransactions(Category category, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("category transactions");
        for (Object json : jsonArray) {
            JSONObject transaction = (JSONObject) json;
            addTransaction(category, transaction);
        }
    }

    // MODIFIES: budget, category
    // EFFECTS: parses transaction from JSON object and adds it to category
    private void addTransaction(Category category, JSONObject jsonObject) {
        String transactionName = jsonObject.getString("transaction name");
        BigDecimal transactionCost = jsonObject.getBigDecimal("transaction cost");
        String transactionDate = jsonObject.getString("transaction date");
        Transaction transaction = new Transaction(transactionName, transactionCost, transactionDate);
        category.addTransaction(transaction);
    }
}
