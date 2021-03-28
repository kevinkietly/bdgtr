package model.exceptions;

import model.Category;

/**
 * Represents the exception that occurs when the given Category already exists in the Budget.
 */
public class DuplicateCategoryException extends DuplicateException {

    public DuplicateCategoryException(Category category) {
        super(category.getName() + " already exists in the budget.");
    }
}
