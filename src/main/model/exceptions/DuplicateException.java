package model.exceptions;

/**
 * Represents general duplicate exceptions that can occur.
 */
public class DuplicateException extends Exception {

    public DuplicateException(String name) {
        super(name + " already exists.");
    }
}
