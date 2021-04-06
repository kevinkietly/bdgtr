package model.exceptions;

/**
 * Represents general empty input exceptions that can occur.
 */
public class EmptyInputException extends Exception {

    public EmptyInputException(String message) {
        super(message);
    }
}
