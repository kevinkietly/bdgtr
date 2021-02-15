package model;

import java.math.BigDecimal;

// Represents a transaction that has a name, cost, and date that the transaction took place
public class Transaction {
    private String transactionName;
    private BigDecimal transactionCost;
    private String transactionDate;

    // REQUIRES: transactionName has a non-zero length, transactionCost > 0
    // EFFECTS: constructs a transaction with a name, cost, and date that the transaction took place
    public Transaction(String transactionName, BigDecimal transactionCost, String transactionDate) {
        this.transactionName = transactionName;
        this.transactionCost = transactionCost;
        this.transactionDate = transactionDate;
    }

    // getters
    public String getTransactionName() {
        return transactionName;
    }

    public BigDecimal getTransactionCost() {
        return transactionCost;
    }

    public String getTransactionDate() {
        return transactionDate;
    }
}
