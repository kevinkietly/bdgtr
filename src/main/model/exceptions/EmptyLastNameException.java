package model.exceptions;

/**
 * Represents the exception that occurs when the last name has length zero.
 */
public class EmptyLastNameException extends EmptyInputException {

    public EmptyLastNameException() {
        super("Last Name cannot be empty.");
    }
}
