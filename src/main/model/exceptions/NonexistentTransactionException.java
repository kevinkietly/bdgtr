package model.exceptions;

import model.Transaction;

/**
 * Represents the exception that occurs when the given Transaction does not exist in the Category.
 */
public class NonexistentTransactionException extends NonexistentException {

    public NonexistentTransactionException(Transaction transaction) {
        super(transaction.getName() + " does not exist in the category.");
    }
}
