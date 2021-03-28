package model.exceptions;

/**
 * Represents the exception that occurs when the given amount is negative.
 */
public class NegativeAmountException extends Exception {

    public NegativeAmountException() {
        super("Amount cannot be negative.");
    }
}
