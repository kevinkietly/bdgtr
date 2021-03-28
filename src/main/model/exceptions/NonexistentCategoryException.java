package model.exceptions;

import model.Category;

/**
 * Represents the exception that occurs when the given Category does not exist in the Budget.
 */
public class NonexistentCategoryException extends NonexistentException {

    public NonexistentCategoryException(Category category) {
        super(category.getName() + " does not exist in the budget.");
    }
}
