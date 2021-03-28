package model.exceptions;

import model.Budget;

/**
 * Represents the exception that occurs when the given Budget already exists in the Account.
 */
public class DuplicateBudgetException extends DuplicateException {

    public DuplicateBudgetException(Budget budget) {
        super(budget.getName() + " already exists in the account.");
    }
}
