package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Represents a category that has a name, amount spent and list of transactions
public class Category implements Writable {
    private String name;                    // the name of the category
    private BigDecimal amountSpent;         // the amount spent in the category
    private List<Transaction> transactions; // the list of transactions in the category

    // REQUIRES: name has a non-zero length
    // EFFECTS: constructs a category with the given name, zero amount spent and empty list of transactions
    public Category(String name) {
        this.name = name;
        this.amountSpent = new BigDecimal("0.00");
        transactions = new ArrayList<>();
    }

    // getters
    public String getName() {
        return name;
    }

    public BigDecimal getAmountSpent() {
        return amountSpent;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    // MODIFIES: this
    // EFFECTS: adds the given transaction to the category
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        amountSpent = amountSpent.add(transaction.getCost());
    }

    // REQUIRES: the given transaction must already be in the category
    // MODIFIES: this
    // EFFECTS: removes the given transaction from the category and updates the amount spent in the category
    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        amountSpent = amountSpent.subtract(transaction.getCost());
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("category name", name);
        json.put("category transactions", transactionsToJson());
        return json;
    }

    // EFFECTS: returns transactions in this category as a JSON array
    public JSONArray transactionsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Transaction transaction : transactions) {
            jsonArray.put(transaction.toJson());
        }

        return jsonArray;
    }
}
