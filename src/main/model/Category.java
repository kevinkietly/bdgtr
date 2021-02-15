package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Represents a category that has a name, amount spent and list of transactions
public class Category {
    private String categoryName;                    // the name of the category
    private BigDecimal categoryAmountSpent;         // the amount spent in the category
    private List<Transaction> categoryTransactions; // the list of transactions in the category

    // REQUIRES: categoryName has a non-zero length
    // EFFECTS: constructs a category with a name, amount spent and empty list of transactions
    public Category(String categoryName) {
        this.categoryName = categoryName;
        this.categoryAmountSpent = new BigDecimal("0.00");
        categoryTransactions = new ArrayList<>();
    }

    // getters
    public String getCategoryName() {
        return categoryName;
    }

    public BigDecimal getCategoryAmountSpent() {
        return categoryAmountSpent;
    }

    public List<Transaction> getCategoryTransactions() {
        return categoryTransactions;
    }

    // MODIFIES: this
    // EFFECTS: adds the given transaction to the category
    public void addTransaction(Transaction transaction) {
        categoryTransactions.add(transaction);
        categoryAmountSpent = categoryAmountSpent.add(transaction.getTransactionCost());
    }

    // REQUIRES: the given transaction must already be in the list of transactions
    // MODIFIES: this
    // EFFECTS: removes the given transaction from the category and updates the amount spent in the category
    public void removeTransaction(Transaction transaction) {
        categoryTransactions.remove(transaction);
        categoryAmountSpent = categoryAmountSpent.subtract(transaction.getTransactionCost());
    }
}
