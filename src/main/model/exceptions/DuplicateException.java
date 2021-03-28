package model.exceptions;

/**
 * Represents general duplicate exceptions that occur.
 */
public class DuplicateException extends Exception {

    public DuplicateException(String message) {
        super(message);
    }
}
