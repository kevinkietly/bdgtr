package model.exceptions;

/**
 * Represents the exception that occurs when the given password has length zero.
 */
public class EmptyPasswordException extends EmptyInputException {

    public EmptyPasswordException() {
        super("Password cannot be empty.");
    }
}
