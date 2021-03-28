package model.exceptions;

/**
 * Represents the exception that occurs when the given input has length zero.
 */
public class EmptyInputException extends Exception {

    public EmptyInputException(String message) {
        super(message);
    }
}
