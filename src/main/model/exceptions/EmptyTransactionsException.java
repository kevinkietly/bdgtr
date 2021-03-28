package model.exceptions;

/**
 * Represents the exception that can occur when the Category has no transactions.
 */
public class EmptyTransactionsException extends EmptyListException {

    public EmptyTransactionsException() {
        super("You do not have any transactions to delete.");
    }
}
