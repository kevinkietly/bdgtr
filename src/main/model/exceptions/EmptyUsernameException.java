package model.exceptions;

/**
 * Represents the exception that occurs when the username has length zero.
 */
public class EmptyUsernameException extends EmptyInputException {

    public EmptyUsernameException() {
        super("Username cannot be empty.");
    }
}
