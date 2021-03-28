package model.exceptions;

/**
 * Represents the exception that occurs when the given cost is negative.
 */
public class NegativeCostException extends Exception {

    public NegativeCostException() {
        super("Cost cannot be negative.");
    }
}
