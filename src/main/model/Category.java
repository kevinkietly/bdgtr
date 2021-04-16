package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a category.
 */
public class Category implements Writable {
    private String name;
    private BigDecimal amountSpent;
    private List<Transaction> transactions;

    /**
     * Constructs a new category with the specified name and no transactions.
     *
     * @param name the name for this category
     * @throws EmptyNameException if the name has length zero
     */
    public Category(String name) throws EmptyNameException {
        if (name.isEmpty()) {
            throw new EmptyNameException();
        }
        this.name = name;
        amountSpent = BigDecimal.ZERO;
        transactions = new ArrayList<>();
    }

    /**
     * Gets the name of this category.
     *
     * @return the name of this category
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the amount spent of this category.
     *
     * @return the amount spent of this category
     */
    public BigDecimal getAmountSpent() {
        return amountSpent;
    }

    /**
     * Gets the transactions in this category.
     *
     * @return the transactions in this category
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Adds the specified transaction to this category.
     *
     * @param transaction the transaction to be added
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        amountSpent = amountSpent.add(transaction.getAmount());
    }

    /**
     * Deletes the specified transaction from this category.
     *
     * @param transaction the transaction to be deleted
     */
    public void deleteTransaction(Transaction transaction) {
        transactions.remove(transaction);
        amountSpent = amountSpent.subtract(transaction.getAmount());
    }

    /**
     * Converts the transactions in this category to JSON.
     *
     * @return the transactions in this category as a JSON array
     */
    public JSONArray transactionsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Transaction nextTransaction : transactions) {
            jsonArray.put(nextTransaction.toJson());
        }
        return jsonArray;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", getName());
        jsonObject.put("amountSpent", getAmountSpent().toString());
        jsonObject.put("transactions", transactionsToJson());
        return jsonObject;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Category category = (Category) object;
        boolean isEqual = true;
        if (!name.equals(category.getName()) || amountSpent.compareTo(category.getAmountSpent()) != 0
                || transactions.size() != category.getTransactions().size()) {
            isEqual = false;
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, amountSpent, transactions);
    }

    @Override
    public String toString() {
        return getName();
    }
}
