package model.exceptions;

/**
 * Represents the exception that occurs when the first name has length zero.
 */
public class EmptyFirstNameException extends EmptyInputException {

    public EmptyFirstNameException() {
        super("First Name cannot be empty.");
    }
}
