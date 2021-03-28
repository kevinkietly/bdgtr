package model;

import model.exceptions.*;
import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;

/**
 * Represents a Transaction.
 */
public class Transaction implements Writable {
    private String name;
    private BigDecimal cost;
    private String date;

    /**
     * Creates a Transaction with the given name, the given cost and the given date.
     *
     * @param name the name for this Transaction
     * @param cost the cost for this Transaction
     * @param date the date for this Transaction
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeCostException if the cost is negative
     */
    public Transaction(String name, BigDecimal cost, String date) throws EmptyNameException, NegativeCostException {
        if (name.isEmpty()) {
            throw new EmptyNameException();
        } else if (cost.compareTo(new BigDecimal("0.00")) < 0) {
            throw new NegativeCostException();
        }
        this.name = name;
        this.cost = cost;
        this.date = date;
    }

    /**
     * Gets the name of this Transaction.
     *
     * @return the name of this Transaction
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the cost of this Transaction.
     *
     * @return the cost of this Transaction
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * Gets the date of this Transaction.
     *
     * @return the date of this Transaction
     */
    public String getDate() {
        return date;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", getName());
        json.put("cost", getCost().toString());
        json.put("date", getDate());
        return json;
    }
}
