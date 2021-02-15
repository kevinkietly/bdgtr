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
        BigDecimal zero = new BigDecimal("0.00");
        return transactionCost.add(zero);
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    // setters
    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public void setTransactionCost(BigDecimal transactionCost) {
        this.transactionCost = transactionCost;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
}
