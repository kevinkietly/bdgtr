package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Represents a Budget.
 */
public class Budget implements Writable {
    private String name;
    private BigDecimal amount;
    private BigDecimal amountSpent;
    private BigDecimal amountRemaining;
    private Calendar startDate;
    private List<Category> categories;
    private List<Category> categoriesToCalculate;
    private List<Category> categoriesToRemove;

    /**
     * Creates a Budget with the given name, the given amount and no categories.
     *
     * @param name the name for this Budget
     * @param amount the amount for this Budget
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     */
    public Budget(String name, BigDecimal amount) throws EmptyNameException, NegativeAmountException {
        if (name.isEmpty()) {
            throw new EmptyNameException();
        } else if (amount.compareTo(new BigDecimal("0.00")) < 0) {
            throw new NegativeAmountException();
        }
        this.name = name;
        this.amount = amount;
        amountSpent = new BigDecimal("0.00");
        amountRemaining = amount;
        startDate = Calendar.getInstance();
        categories = new ArrayList<>();
        categoriesToCalculate = new ArrayList<>();
        categoriesToRemove = new ArrayList<>();
    }

    /**
     * Gets the name of this Budget.
     *
     * @return the name of this Budget
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the amount of this Budget.
     *
     * @return the amount of this Budget
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Gets the amount spent of this Budget.
     *
     * @return the amount spent of this Budget
     */
    public BigDecimal getAmountSpent() {
        return amountSpent;
    }

    /**
     * Gets the amount remaining of this Budget.
     *
     * @return the amount remaining of this Budget
     */
    public BigDecimal getAmountRemaining() {
        return amountRemaining;
    }

    /**
     * Gets the start date of this Budget.
     *
     * @return the start date of this Budget
     */
    public String getStartDate() {
        return startDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " "
                + startDate.get(Calendar.DAY_OF_MONTH) + ", "
                + startDate.get(Calendar.YEAR);
    }

    /**
     * Gets the categories in this Budget.
     *
     * @return the categories in this Budget
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Gets the categories to calculate in this Budget.
     *
     * @return the categories to calculate in this Budget
     */
    public List<Category> getCategoriesToCalculate() {
        return categoriesToCalculate;
    }

    /**
     * Gets the categories to remove from the categories to calculate in this Budget.
     *
     * @return the categories to remove from the categories to calculate in this Budget
     */
    public List<Category> getCategoriesToRemove() {
        return categoriesToRemove;
    }

    /**
     * Adds the given Category to this Budget.
     *
     * @param category the Category to be added
     * @throws DuplicateCategoryException if the Category already exists in this Budget
     */
    public void addCategory(Category category) throws DuplicateCategoryException {
        if (categories.size() != 0) {
            for (Category nextCategory : categories) {
                if (nextCategory.getName().equals(category.getName())) {
                    throw new DuplicateCategoryException(category);
                }
            }
        }
        categories.add(category);
        categoriesToCalculate.add(category);
        calculateAmountRemaining();
    }

    /**
     * Deletes the given Category from this Budget.
     *
     * @param category the Category to be deleted
     * @throws NonexistentCategoryException if the Category does not exist in this Budget
     * @throws EmptyCategoriesException if this Budget has no categories
     */
    public void deleteCategory(Category category) throws NonexistentCategoryException, EmptyCategoriesException {
        boolean isDeleted = false;
        if (categories.size() != 0) {
            for (Category nextCategory : categories) {
                if (nextCategory.getName().equals(category.getName())) {
                    categories.remove(category);
                    categoriesToCalculate.remove(category);
                    isDeleted = true;
                    amountSpent = amountSpent.subtract(category.getAmountSpent());
                    amountRemaining = amountRemaining.add(category.getAmountSpent());
                    break;
                }
            }
            if (!isDeleted) {
                throw new NonexistentCategoryException(category);
            }
        } else {
            throw new EmptyCategoriesException();
        }
    }

    /**
     * Calculates the amount spent of this Budget.
     *
     * @return the calculated amount spent of this Budget
     */
    public BigDecimal calculateAmountSpent() {
        if (categoriesToCalculate.size() == 0) {
            for (Category nextCategory : categories) {
                amountSpent = amountSpent.add(nextCategory.getAmountSpent());
            }
        }
        for (Category nextCategory : categoriesToCalculate) {
            amountSpent = amountSpent.add(nextCategory.getAmountSpent());
            categoriesToRemove.add(nextCategory);
        }
        for (Category nextCategory : categoriesToRemove) {
            categoriesToCalculate.remove(nextCategory);
        }
        categoriesToRemove.clear();
        return amountSpent;
    }

    /**
     * Calculates the amount remaining of this Budget.
     *
     * @return the calculated amount remaining of this Budget
     */
    public BigDecimal calculateAmountRemaining() {
        calculateAmountSpent();
        return amountRemaining = amount.subtract(amountSpent);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", getName());
        json.put("amount", getAmount().toString());
        json.put("amountSpent", getAmountSpent().toString());
        json.put("amountRemaining", getAmountRemaining().toString());
        json.put("startDate", getStartDate());
        json.put("categories", categoriesToJson());
        return json;
    }

    /**
     * Converts categories in this Budget to JSON.
     *
     * @return the categories in this Budget as a JSON array
     */
    public JSONArray categoriesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Category nextCategory : categories) {
            jsonArray.put(nextCategory.toJson());
        }
        return jsonArray;
    }
}
