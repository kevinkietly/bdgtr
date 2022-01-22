package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Represents an account.
 */
public class Account implements Writable {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private List<Budget> budgets;
    private boolean autoSave;

    /**
     * Constructs a new account with the specified first name, last name, username, password, and no budgets.
     *
     * @param firstName the first name for this account
     * @param lastName the last name for this account
     * @param username the username for this account
     * @param password the password for this account
     * @throws EmptyFirstNameException if the first name has length zero
     * @throws EmptyLastNameException if the last name has length zero
     * @throws EmptyUsernameException if the username has length zero
     * @throws EmptyPasswordException if the password has length zero
     */
    public Account(String firstName, String lastName, String username, String password) throws EmptyFirstNameException,
            EmptyLastNameException, EmptyUsernameException, EmptyPasswordException {
        if (firstName.isEmpty()) {
            throw new EmptyFirstNameException();
        } else if (lastName.isEmpty()) {
            throw new EmptyLastNameException();
        } else if (username.isEmpty()) {
            throw new EmptyUsernameException();
        } else if (password.isEmpty()) {
            throw new EmptyPasswordException();
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        budgets = new ArrayList<>();
        autoSave = false;
    }

    /**
     * Gets the first name of this account.
     *
     * @return the first name of this account
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of this account.
     *
     * @return the last name of this account
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the username of this account.
     *
     * @return the username of this account
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of this account.
     *
     * @return the password of this account
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the budgets in this account.
     *
     * @return the budgets in this account
     */
    public List<Budget> getBudgets() {
        return budgets;
    }

    /**
     * Determines the Auto Save state for this account.
     *
     * @return the Auto Save state for this account
     */
    public boolean isAutoSave() {
        return autoSave;
    }

    /**
     * Sets the first name for this account to the specified first name.
     *
     * @param firstName the first name to be set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name for this account to the specified last name.
     *
     * @param lastName the last name to be set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the username for this account to the specified username.
     *
     * @param username the username to be set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password for this account to the specified password.
     *
     * @param password the password to be set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the Auto Save state for this account to either true or false.
     *
     * @param autoSave true or false
     */
    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    /**
     * Adds the specified budget to this account.
     *
     * @param budget the budget to be added
     * @throws DuplicateBudgetException if the budget already exists in this account
     */
    public void addBudget(Budget budget) throws DuplicateBudgetException {
        if (budgets.size() > 0) {
            for (Budget nextBudget : budgets) {
                if (nextBudget.getName().equals(budget.getName())) {
                    throw new DuplicateBudgetException(budget);
                }
            }
        }
        budgets.add(budget);
    }

    /**
     * Deletes the specified budget from this account.
     *
     * @param budget the budget to be deleted
     */
    public void deleteBudget(Budget budget) {
        budgets.remove(budget);
    }

    /**
     * Converts the budgets in this account to JSON.
     *
     * @return the budgets in this account as a JSON array
     */
    public JSONArray budgetsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Budget nextBudget : budgets) {
            jsonArray.put(nextBudget.toJson());
        }
        return jsonArray;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", getFirstName());
        jsonObject.put("lastName", getLastName());
        jsonObject.put("username", getUsername());
        jsonObject.put("password", getPassword());
        jsonObject.put("budgets", budgetsToJson());
        jsonObject.put("autoSave", autoSave);
        return jsonObject;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Account account = (Account) object;
        if (!firstName.equals(account.getFirstName()) || !lastName.equals(account.getLastName())
                || !username.equals(account.getUsername()) || !password.equals(account.getPassword())
                || budgets.size() != account.getBudgets().size() || autoSave != account.isAutoSave()) {
            return false;
        } else {
            Iterator<Budget> thisIterator = budgets.iterator();
            Iterator<Budget> objectIterator = account.getBudgets().iterator();
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
        return Objects.hash(firstName, lastName, username, password, budgets, autoSave);
    }
}
