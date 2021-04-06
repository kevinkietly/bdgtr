package model.exceptions;

/**
 * Represents the exception that occurs when the amount is negative.
 */
public class NegativeAmountException extends AmountException {

    public NegativeAmountException() {
        super("negative");
    }
}
