package model.exceptions;

/**
 * Represents the exception that can occur when the Budget has no categories.
 */
public class EmptyCategoriesException extends EmptyListException {

    public EmptyCategoriesException() {
        super("You do not have any categories to delete.");
    }
}
