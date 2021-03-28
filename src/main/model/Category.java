package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Category.
 */
public class Category implements Writable {
    private String name;
    private BigDecimal amountSpent;
    private List<Transaction> transactions;

    /**
     * Creates a Category with the given name and no transactions.
     *
     * @param name the name for this Category
     * @throws EmptyNameException if the name has length zero
     */
    public Category(String name) throws EmptyNameException {
        if (name.isEmpty()) {
            throw new EmptyNameException();
        }
        this.name = name;
        amountSpent = new BigDecimal("0.00");
        transactions = new ArrayList<>();
    }

    /**
     * Gets the name of this Category.
     *
     * @return the name of this Category
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the amount spent of this Category.
     *
     * @return the amount spent of this Category
     */
    public BigDecimal getAmountSpent() {
        return amountSpent;
    }

    /**
     * Gets the transactions in this Category.
     *
     * @return the transactions in this Category
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Adds the given Transaction to this Category.
     *
     * @param transaction the Transaction to be added
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        amountSpent = amountSpent.add(transaction.getCost());
    }

    /**
     * Deletes the given Transaction from this Category.
     *
     * @param transaction the Transaction to be deleted
     * @throws NonexistentTransactionException if the Transaction does not exist in this Category
     * @throws EmptyTransactionsException if this Category has no transactions
     */
    public void deleteTransaction(Transaction transaction) throws NonexistentTransactionException,
            EmptyTransactionsException {
        boolean isDeleted = false;
        if (transactions.size() != 0) {
            for (Transaction nextTransaction : transactions) {
                if (nextTransaction.getName().equals(transaction.getName())) {
                    transactions.remove(transaction);
                    isDeleted = true;
                    amountSpent = amountSpent.subtract(transaction.getCost());
                    break;
                }
            }
            if (!isDeleted) {
                throw new NonexistentTransactionException(transaction);
            }
        } else {
            throw new EmptyTransactionsException();
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", getName());
        json.put("amountSpent", getAmountSpent());
        json.put("transactions", transactionsToJson());
        return json;
    }

    /**
     * Converts transactions in this Category to JSON.
     *
     * @return the transactions in this Category as a JSON array
     */
    public JSONArray transactionsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Transaction nextTransaction : transactions) {
            jsonArray.put(nextTransaction.toJson());
        }
        return jsonArray;
    }
}
