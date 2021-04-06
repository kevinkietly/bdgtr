package model.exceptions;

/**
 * Represents general amount exceptions that can occur.
 */
public class AmountException extends Exception {

    public AmountException(String message) {
        super("Amount cannot be " + message + ".");
    }
}
