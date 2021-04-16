package persistence;

import model.*;
import model.exceptions.*;
import org.json.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Represents a reader that reads an account from JSON data stored in file.
 * Code referenced from:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonReader {
    private String source;

    /**
     * Constructs a new reader to read from the specified source file.
     *
     * @param source the source file
     */
    public JsonReader(String source) {
        this.source = source;
    }

    /**
     * Reads the account with the specified username.
     *
     * @param accountUsername the username of the account to be read
     * @return the account with the given username
     * @throws IOException if an error occurs reading data from file
     * @throws EmptyFirstNameException if the first name has length zero
     * @throws EmptyLastNameException if the last name has length zero
     * @throws EmptyUsernameException if the username has length zero
     * @throws EmptyPasswordException if the password has length zero
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     * @throws DuplicateBudgetException if the budget already exists in the account
     * @throws DuplicateCategoryException if the category already exists in the budget
     */
    public Account read(String accountUsername) throws IOException, EmptyFirstNameException, EmptyLastNameException,
            EmptyUsernameException, EmptyPasswordException, EmptyNameException, NegativeAmountException,
            ZeroAmountException, DuplicateBudgetException, DuplicateCategoryException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAccount(accountUsername, jsonObject);
    }

    /**
     * Reads the specified source file as string.
     *
     * @param source the source file
     * @return the source file as string
     * @throws IOException if an error occurs reading data from file
     */
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    /**
     * Parses the account with the specified username from the specified JSON object.
     *
     * @param accountUsername the username of the account to be parsed
     * @param jsonObject the JSON object
     * @return the parsed account with the specified username
     * @throws EmptyFirstNameException if the first name has length zero
     * @throws EmptyLastNameException if the last name has length zero
     * @throws EmptyUsernameException if the username has length zero
     * @throws EmptyPasswordException if the password has length zero
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     * @throws DuplicateBudgetException if the budget already exists in the account
     * @throws DuplicateCategoryException if the category already exists in the budget
     */
    private Account parseAccount(String accountUsername, JSONObject jsonObject) throws EmptyFirstNameException,
            EmptyLastNameException, EmptyUsernameException, EmptyPasswordException, EmptyNameException,
            NegativeAmountException, ZeroAmountException, DuplicateBudgetException, DuplicateCategoryException {
        JSONObject accountJsonObject = jsonObject.getJSONObject(accountUsername);
        String firstName = accountJsonObject.getString("firstName");
        String lastName = accountJsonObject.getString("lastName");
        String username = accountJsonObject.getString("username");
        String password = accountJsonObject.getString("password");
        Account account = new Account(firstName, lastName, username, password);
        addBudgets(accountJsonObject, account);
        return account;
    }

    /**
     * Parses the budgets from the specified account JSON object and adds them to the specified account.
     *
     * @param accountJsonObject the account JSON object
     * @param account the account to which the parsed budgets will be added
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     * @throws DuplicateBudgetException if the budget already exists in the account
     * @throws DuplicateCategoryException if the category already exists in the budget
     */
    private void addBudgets(JSONObject accountJsonObject, Account account) throws EmptyNameException,
            NegativeAmountException, ZeroAmountException, DuplicateBudgetException, DuplicateCategoryException {
        JSONArray jsonArray = accountJsonObject.getJSONArray("budgets");
        for (Object nextObject : jsonArray) {
            JSONObject budgetJsonObject = (JSONObject) nextObject;
            addBudget(budgetJsonObject, account);
        }
    }

    /**
     * Parses the specified budget JSON object and adds it to the list of budgets in the specified account.
     *
     * @param budgetJsonObject the budget JSON object
     * @param account the account with the list of budgets to which the parsed budget will be added
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     * @throws DuplicateBudgetException if the budget already exists in the account
     * @throws DuplicateCategoryException if the category already exists in the budget
     */
    private void addBudget(JSONObject budgetJsonObject, Account account) throws EmptyNameException,
            NegativeAmountException, ZeroAmountException, DuplicateBudgetException, DuplicateCategoryException {
        String name = budgetJsonObject.getString("name");
        BigDecimal amount = budgetJsonObject.getBigDecimal("amount");
        String startDate = budgetJsonObject.getString("startDate");
        Budget budget = new Budget(name, amount);
        budget.setStartDate(startDate);
        addCategories(budgetJsonObject, budget);
        account.addBudget(budget);
    }

    /**
     * Parses the categories from the specified budget JSON object and adds them to the specified budget.
     *
     * @param budgetJsonObject the budget JSON object
     * @param budget the budget to which the parsed categories will be added
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     * @throws DuplicateCategoryException if the category already exists in the budget
     */
    private void addCategories(JSONObject budgetJsonObject, Budget budget) throws EmptyNameException,
            NegativeAmountException, ZeroAmountException, DuplicateCategoryException {
        JSONArray jsonArray = budgetJsonObject.getJSONArray("categories");
        for (Object nextObject : jsonArray) {
            JSONObject categoryJsonObject = (JSONObject) nextObject;
            addCategory(categoryJsonObject, budget);
        }
        budget.calculateAmountRemaining();
    }

    /**
     * Parses the specified category JSON object and adds it to the list of categories in the specified budget.
     *
     * @param categoryJsonObject the category JSON object
     * @param budget the budget with the list of categories to which the parsed category will be added
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     * @throws DuplicateCategoryException if the category already exists in the budget
     */
    private void addCategory(JSONObject categoryJsonObject, Budget budget) throws EmptyNameException,
            NegativeAmountException, ZeroAmountException, DuplicateCategoryException {
        String name = categoryJsonObject.getString("name");
        Category category = new Category(name);
        addTransactions(categoryJsonObject, category);
        budget.addCategory(category);
    }

    /**
     * Parses the transactions from the specified category JSON object and adds them to the specified category.
     *
     * @param categoryJsonObject the category JSON object
     * @param category the category to which the parsed transactions will be added
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     */
    private void addTransactions(JSONObject categoryJsonObject, Category category) throws EmptyNameException,
            NegativeAmountException, ZeroAmountException {
        JSONArray jsonArray = categoryJsonObject.getJSONArray("transactions");
        for (Object nextObject : jsonArray) {
            JSONObject transactionJsonObject = (JSONObject) nextObject;
            addTransaction(transactionJsonObject, category);
        }
    }

    /**
     * Parses the specified transaction JSON object and adds it to the list of transactions in the specified category.
     *
     * @param transactionJsonObject the transaction JSON object
     * @param category the category with the list of transactions to which the parsed transaction will be added
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     */
    private void addTransaction(JSONObject transactionJsonObject, Category category) throws EmptyNameException,
            NegativeAmountException, ZeroAmountException {
        String name = transactionJsonObject.getString("name");
        BigDecimal amount = transactionJsonObject.getBigDecimal("amount");
        String date = transactionJsonObject.getString("date");
        Transaction transaction = new Transaction(name, amount, date);
        category.addTransaction(transaction);
    }
}
