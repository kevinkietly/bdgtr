package model.exceptions;

/**
 * Represents the exception that can occur when the Account has no budgets.
 */
public class EmptyBudgetsException extends EmptyListException {

    public EmptyBudgetsException() {
        super("You do not have any budgets to delete.");
    }
}
