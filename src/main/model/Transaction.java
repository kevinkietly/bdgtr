package model;

import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;

// Represents a transaction that has a name, cost, and date that the transaction took place
public class Transaction implements Writable {
    private String name;      // the name of the transaction
    private BigDecimal cost;  // the cost of the transaction
    private String date;      // the date of the transaction

    // REQUIRES: name has a non-zero length, cost > 0
    // EFFECTS: constructs a transaction with the given name, given cost, and given date that the transaction took place
    public Transaction(String name, BigDecimal cost, String date) {
        this.name = name;
        this.cost = cost;
        this.date = date;
    }

    // getters
    public String getName() {
        return name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getDate() {
        return date;
    }

    // setters
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("transaction name", name);
        json.put("transaction cost", cost.toString());
        json.put("transaction date", date);
        return json;
    }
}
