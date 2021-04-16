package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;
import java.util.*;

/**
 * Represents a budget.
 */
public class Budget implements Writable {
    private String name;
    private BigDecimal amount;
    private BigDecimal amountSpent;
    private BigDecimal amountRemaining;
    private String startDate;
    private List<Category> categories;

    /**
     * Constructs a new budget with the specified name, amount, and no categories.
     *
     * @param name the name for this budget
     * @param amount the amount for this budget
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     */
    public Budget(String name, BigDecimal amount) throws EmptyNameException, NegativeAmountException,
            ZeroAmountException {
        if (name.isEmpty()) {
            throw new EmptyNameException();
        } else if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeAmountException();
        } else if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new ZeroAmountException();
        }
        this.name = name;
        this.amount = amount;
        amountSpent = BigDecimal.ZERO;
        amountRemaining = amount;
        Calendar rightNow = Calendar.getInstance();
        startDate = rightNow.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " "
                + rightNow.get(Calendar.DAY_OF_MONTH) + ", "
                + rightNow.get(Calendar.YEAR);
        categories = new ArrayList<>();
    }

    /**
     * Gets the name of this budget.
     *
     * @return the name of this budget
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the amount of this budget.
     *
     * @return the amount of this budget
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Gets the amount spent of this budget.
     *
     * @return the amount spent of this budget
     */
    public BigDecimal getAmountSpent() {
        return amountSpent;
    }

    /**
     * Gets the amount remaining of this budget.
     *
     * @return the amount remaining of this budget
     */
    public BigDecimal getAmountRemaining() {
        return amountRemaining;
    }

    /**
     * Gets the start date of this budget.
     *
     * @return the start date of this budget
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Gets the categories in this budget.
     *
     * @return the categories in this budget
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Sets the start date of this budget to the specified date.
     *
     * @param date the date to be set
     */
    public void setStartDate(String date) {
        startDate = date;
    }

    /**
     * Adds the specified category to this budget.
     *
     * @param category the category to be added
     * @throws DuplicateCategoryException if the category already exists in this budget
     */
    public void addCategory(Category category) throws DuplicateCategoryException {
        if (categories.size() > 0) {
            for (Category nextCategory : categories) {
                if (nextCategory.getName().equals(category.getName())) {
                    throw new DuplicateCategoryException(category);
                }
            }
        }
        categories.add(category);
    }

    /**
     * Deletes the specified category from this budget.
     *
     * @param category the category to be deleted
     */
    public void deleteCategory(Category category) {
        categories.remove(category);
        amountSpent = amountSpent.subtract(category.getAmountSpent());
        amountRemaining = amountRemaining.add(category.getAmountSpent());
    }

    /**
     * Calculates the amount spent of this budget.
     *
     * @return the calculated amount spent of this budget
     */
    public BigDecimal calculateAmountSpent() {
        amountSpent = BigDecimal.ZERO;
        for (Category nextCategory : categories) {
            amountSpent = amountSpent.add(nextCategory.getAmountSpent());
        }
        return amountSpent;
    }

    /**
     * Calculates the amount remaining of this budget.
     *
     * @return the calculated amount remaining of this budget
     */
    public BigDecimal calculateAmountRemaining() {
        calculateAmountSpent();
        return amountRemaining = amount.subtract(amountSpent);
    }

    /**
     * Returns the number of transactions in this budget.
     *
     * @return the number of transactions in this budget
     */
    public int numberOfTransactions() {
        int numberOfTransactions = 0;
        for (Category nextCategory : categories) {
            numberOfTransactions += nextCategory.getTransactions().size();
        }
        return numberOfTransactions;
    }

    /**
     * Converts the categories in this budget to JSON.
     *
     * @return the categories in this budget as a JSON array
     */
    public JSONArray categoriesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Category nextCategory : categories) {
            jsonArray.put(nextCategory.toJson());
        }
        return jsonArray;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", getName());
        jsonObject.put("amount", getAmount().toString());
        jsonObject.put("amountSpent", getAmountSpent().toString());
        jsonObject.put("amountRemaining", getAmountRemaining().toString());
        jsonObject.put("startDate", getStartDate());
        jsonObject.put("categories", categoriesToJson());
        return jsonObject;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Budget budget = (Budget) object;
        if (!name.equals(budget.getName()) || amount.compareTo(budget.getAmount()) != 0
                || amountSpent.compareTo(budget.getAmountSpent()) != 0
                || amountRemaining.compareTo(budget.getAmountRemaining()) != 0
                || !getStartDate().equals(budget.getStartDate())
                || categories.size() != budget.getCategories().size()) {
            return false;
        } else {
            Iterator<Category> thisIterator = categories.iterator();
            Iterator<Category> objectIterator = budget.getCategories().iterator();
            boolean isEqual = true;
            while (thisIterator.hasNext() && objectIterator.hasNext()) {
                if (!thisIterator.next().equals(objectIterator.next())) {
                    isEqual = false;
                }
            }
            return isEqual;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, amount, amountSpent, amountRemaining, startDate, categories);
    }

    @Override
    public String toString() {
        return getName();
    }
}
