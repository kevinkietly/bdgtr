package model;

import model.exceptions.*;
import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a transaction.
 */
public class Transaction implements Writable {
    private String name;
    private BigDecimal amount;
    private String date;

    /**
     * Constructs a new transaction with the specified name, amount, and date.
     *
     * @param name the name for this transaction
     * @param amount the amount for this transaction
     * @param date the date for this transaction
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     */
    public Transaction(String name, BigDecimal amount, String date) throws EmptyNameException, NegativeAmountException,
            ZeroAmountException {
        if (name.isEmpty()) {
            throw new EmptyNameException();
        } else if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeAmountException();
        } else if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new ZeroAmountException();
        }
        this.name = name;
        this.amount = amount;
        this.date = date;
    }

    /**
     * Gets the name of this transaction.
     *
     * @return the name of this transaction
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the amount of this transaction.
     *
     * @return the amount of this transaction
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Gets the date of this transaction.
     *
     * @return the date of this transaction
     */
    public String getDate() {
        return date;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", getName());
        jsonObject.put("amount", getAmount().toString());
        jsonObject.put("date", getDate());
        return jsonObject;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Transaction transaction = (Transaction) object;
        boolean isEqual = true;
        if (!name.equals(transaction.getName()) || amount.compareTo(transaction.getAmount()) != 0
                || !date.equals(transaction.getDate())) {
            isEqual = false;
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, amount, date);
    }

    @Override
    public String toString() {
        return getName();
    }
}
