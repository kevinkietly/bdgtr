package model.exceptions;

/**
 * Represents the exception that occurs when the given name has length zero.
 */
public class EmptyNameException extends EmptyInputException {

    public EmptyNameException() {
        super("Name cannot be empty.");
    }
}
