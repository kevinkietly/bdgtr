package model.exceptions;

/**
 * Represents the exception that occurs when the amount is zero.
 */
public class ZeroAmountException extends AmountException {

    public ZeroAmountException() {
        super("zero");
    }
}
