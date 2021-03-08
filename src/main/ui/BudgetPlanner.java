package ui;

import model.*;
import persistence.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

// Budget Planner application
// Code referenced from https://github.students.cs.ubc.ca/CPSC210/TellerApp.git
public class BudgetPlanner extends Formatter {
    private static final String MENU_COMMAND = "menu";
    private static final String QUIT_COMMAND = "quit";
    private static final String ADD_BUDGET_COMMAND = "add budget";
    private static final String VIEW_ACCOUNT_COMMAND = "view account";
    private static final String ADD_CATEGORY_COMMAND = "add category";
    private static final String ADD_TRANSACTION_COMMAND = "add transaction";
    private static final String JSON_STORE = "./data/account.json";
    private static final String SAVE_ACCOUNT_COMMAND = "save account";
    private static final String SIGN_IN_COMMAND = "sign in";
    private static final String CREATE_ACCOUNT_COMMAND = "create account";

    private Account account;
    private Budget budget;
    private Category category;
    private Transaction transaction;
    private Scanner input;
    private boolean keepGoing;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the Budget Planner application
    public BudgetPlanner() {
        keepGoing = true;
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runProgram();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runProgram() {
        String command;

        input = new Scanner(System.in);

        displayLandingPage();

        while (keepGoing) {
            command = input.nextLine();
            command = command.toLowerCase();
            processCommand(command);
        }
        System.out.println("Goodbye.");
    }

    // EFFECTS: displays landing page to user
    private void displayLandingPage() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("|                                  Budget Planner                                  |");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("| Welcome to Budget Planner.                                                       |");
        System.out.println("| If you have an account, enter '" + SIGN_IN_COMMAND
                + "' to sign in and load your account.        |");
        System.out.println("| If you do not have an account, enter '" + CREATE_ACCOUNT_COMMAND
                + "' to create an account.      |");
        System.out.println("------------------------------------------------------------------------------------");
    }

    // EFFECTS: displays options menu to user
    private void displayOptionsMenu() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("|                                   Options Menu                                   |");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("| To view your account, enter '" + VIEW_ACCOUNT_COMMAND
                + "'.                                      |");
        System.out.println("| To add a new budget, enter '" + ADD_BUDGET_COMMAND
                + "'.                                         |");
        System.out.println("| To delete a budget, enter 'delete budget' and the budget name.                   |");
        System.out.println("| To save your account to file, enter '" + SAVE_ACCOUNT_COMMAND
                + "'.                              |");
        System.out.println("| To quit Budget Planner, enter '" + QUIT_COMMAND
                + "'.                                            |");
        System.out.println("------------------------------------------------------------------------------------");
    }

    // EFFECTS: displays all budgets to user
    private void displayBudgets() {
        if (account.getBudgets().size() == 0) {
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("|                                     Budgets                                      |");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("| You do not have any budgets. To create one, enter '" + ADD_BUDGET_COMMAND
                    + "'.                  |");
            System.out.println("------------------------------------------------------------------------------------");
        } else {
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("|                                     Budgets                                      |");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.format("%-16s %-16s %-16s %-16s %-16s", "| Name", "| Amount", "| Spent", "| Remaining",
                    "| Date Created |");
            System.out.println("\n------------------------------------------------------------------------------------"
                    + "");
            printBudgets();
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("| To access a budget, enter the budget's name.                                     |");
            System.out.println("------------------------------------------------------------------------------------");
            appendViewOptionsMenuInstruction();
        }
    }

    // MODIFIES: this
    // EFFECTS: prints all budgets in account
    public void printBudgets() {
        for (Budget nextBudget : account.getBudgets()) {
            System.out.format("%-16s %-16s %-16s %-16s %-16s", "| ", "| ", "| ", "| ", "| " + "             |");
            System.out.format("\n%-16s %-16s %-16s %-16s %-16s", "| " + nextBudget.getName(),
                    "| $" + nextBudget.getAmount(), "| $" + nextBudget.getAmountSpent(), "| $"
                            + nextBudget.getAmountRemaining(), "| "
                            + formatBudgetStartingDate(nextBudget.formatStartingDate()));
            System.out.println("\n---------------------------------------------------------------------------------"
                    + "---");
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to sign in to their account
    private void signIn() {
        System.out.print("Username: ");
        String username = input.nextLine();
        System.out.print("Password: ");
        String password = input.nextLine();
        while (!loadAccount(username, password)) {
            if (username.equals(account.getUsername())) {
                System.out.println("Wrong password. Try again.");
                System.out.print("Password: ");
                password = input.nextLine();
            } else if (password.equals(account.getPassword())) {
                System.out.println("Wrong username. Try again.");
                System.out.print("Username: ");
                username = input.nextLine();
            } else {
                System.out.println("Couldn't find your account. Enter '" + SIGN_IN_COMMAND
                        + "' to try again or '" + CREATE_ACCOUNT_COMMAND + "' to create an account.");
                break;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to create an account
    private void createAccount() {
        if (account == null) {
            System.out.println("Create your account by first entering your username and then your password.");
            System.out.print("Username: ");
            String username = input.nextLine();
            System.out.print("Password: ");
            String password = input.nextLine();
            account = new Account(username, password);
            System.out.println("Account successfully created.");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.format("%-48s %-48s", "| ACCOUNT: " + account.getUsername(),
                    "                                  |");
            System.out.println("\n------------------------------------------------------------------------------------"
                    + "");
            displayOptionsMenu();
        } else {
            System.out.println("You are currently signed in to '" + account.getUsername()
                    + "'. You can only create one account.");
        }
    }

    // MODIFIES: this
    // EFFECTS: displays account to user
    private void displayAccount() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.format("%-48s %-48s", "| ACCOUNT: " + account.getUsername(),
                "                                  |");
        System.out.println("\n------------------------------------------------------------------------------------");
        displayBudgets();
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.length() > 0) {
            if (command.equals(MENU_COMMAND)) {
                displayOptionsMenu();
            } else if (command.equals(QUIT_COMMAND)) {
                quitBudgetPlanner();
            } else if (command.equals(ADD_BUDGET_COMMAND)) {
                createAndAddBudget();
            } else if (isDeleteBudget(command)) {
                updateAccountDeleteBudget();
            } else if (command.equals(VIEW_ACCOUNT_COMMAND)) {
                displayAccount();
            } else if (command.equals(ADD_CATEGORY_COMMAND)) {
                createAndAddCategory();
            } else if (isDeleteCategory(command)) {
                updateBudgetDeleteCategory();
            } else if (command.equals(ADD_TRANSACTION_COMMAND)) {
                createAndAddTransaction();
            } else if (isDeleteTransaction(command)) {
                updateCategoryDeleteTransaction();
            } else {
                processMoreCommands(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes more user commands
    public void processMoreCommands(String command) {
        if (command.length() > 0) {
            if (isInBudgets(command)) {
                setBudget(budget);
            } else if (isInBudget(command)) {
                setCategory(category);
            } else if (command.equals(SAVE_ACCOUNT_COMMAND)) {
                saveAccount();
            } else if (command.equals(SIGN_IN_COMMAND)) {
                signIn();
            } else if (command.equals(CREATE_ACCOUNT_COMMAND)) {
                createAccount();
            } else {
                System.out.println("Invalid command. Try again.");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: if user wants to save, save account and quit, otherwise quit
    private void quitBudgetPlanner() {
        System.out.println("Want to save changes to your account to '" + JSON_STORE + "'? Enter 'yes' or 'no'.");
        if (input.nextLine().equals("yes")) {
            saveAccount();
        }
        keepGoing = false;
    }

    // EFFECTS: returns true if the given input to delete a budget matches a budget in the account, false otherwise
    private boolean isDeleteBudget(String command) {
        boolean deleteBudget = false;
        if (account == null) {
            return false;
        } else {
            for (Budget nextBudget : account.getBudgets()) {
                if (command.equalsIgnoreCase("delete budget " + nextBudget.getName())) {
                    deleteBudget = true;
                    this.budget = nextBudget;
                    break;
                }
            }
        }
        if (command.contains("delete budget ") && !deleteBudget) {
            System.out.println("The budget you want to delete is not in the account.");
        }
        return deleteBudget;
    }

    // MODIFIES: this
    // EFFECTS: updates the account after a budget is deleted
    public void updateAccountDeleteBudget() {
        account.removeBudget(budget);
        System.out.println("Budget '" + budget.getName() + "' successfully deleted.");
        this.category = null;
        displayAccount();
    }

    // EFFECTS: returns true if the given input to delete a category matches a category in the budget, false otherwise
    public boolean isDeleteCategory(String command) {
        boolean deleteCategory = false;
        if (budget == null) {
            return false;
        } else {
            for (Category nextCategory : budget.getCategoriesToDisplay()) {
                if (command.equalsIgnoreCase("delete category " + nextCategory.getName())) {
                    deleteCategory = true;
                    this.category = nextCategory;
                    break;
                }
            }
        }
        if (command.contains("delete category ") && !deleteCategory) {
            System.out.println("The category you want to delete is not in the budget.");
        }
        return deleteCategory;
    }

    // MODIFIES: this
    // EFFECTS: updates the budget after a category is deleted
    public void updateBudgetDeleteCategory() {
        budget.removeCategory(category);
        System.out.println("Category '" + category.getName() + "' successfully deleted.");
        this.category = null;
        displayEverythingEmptyCategoriesAndTransactions();
    }

    // MODIFIES: this
    // EFFECTS: returns true if the given input to delete a transaction matches a transaction in the category, false
    // otherwise
    public boolean isDeleteTransaction(String command) {
        boolean deleteTransaction = false;
        if (category == null) {
            return false;
        } else {
            for (Transaction nextTransaction : category.getTransactions()) {
                if (command.equalsIgnoreCase("delete transaction " + nextTransaction.getName())) {
                    deleteTransaction = true;
                    this.transaction = nextTransaction;
                    break;
                }
            }
        }
        if (command.contains("delete transaction ") && !deleteTransaction) {
            System.out.println("The transaction you want to delete is not in the budget.");
        }
        return deleteTransaction;
    }

    // MODIFIES: this
    // EFFECTS: updates the category after a transaction is deleted
    public void updateCategoryDeleteTransaction() {
        category.removeTransaction(transaction);
        System.out.println("Transaction '" + transaction.getName() + "' successfully deleted.");
        budget.calculateAmountRemaining();
        displayEverythingEmptyCategoriesAndTransactions();
    }

    // EFFECTS: returns true if the given input matches a budget in the account, false otherwise
    public boolean isInBudgets(String command) {
        boolean containsBudget = false;
        if (account == null) {
            return false;
        } else {
            for (Budget nextBudget : account.getBudgets()) {
                if (command.equalsIgnoreCase(nextBudget.getName())) {
                    containsBudget = true;
                    this.budget = nextBudget;
                    break;
                }
            }
        }
        return containsBudget;
    }

    // MODIFIES: this
    // EFFECTS: sets the selected budget to the given budget
    public void setBudget(Budget budget) {
        this.budget = budget;
        displayEverythingEmptyCategoriesAndTransactions();
    }

    // EFFECTS: returns true if the given input matches a category in the budget, false otherwise
    public boolean isInBudget(String command) {
        boolean containsCategory = false;
        if (budget == null) {
            return false;
        } else {
            for (Category nextCategory : budget.getCategoriesToDisplay()) {
                if (command.equalsIgnoreCase(nextCategory.getName())) {
                    containsCategory = true;
                    this.category = nextCategory;
                    break;
                }
            }
        }
        return containsCategory;
    }

    // MODIFIES: this
    // EFFECTS: sets the selected category to the given budget
    public void setCategory(Category category) {
        this.category = category;
        displayEverythingEmptyCategoriesAndTransactions();
    }

    // REQUIRES: the amount of the budget must be a number
    // MODIFIES: this
    // EFFECTS: creates a new budget
    private void createAndAddBudget() {
        System.out.print("Enter the name of your budget: ");
        String budgetName = input.nextLine();
        System.out.print("Enter the amount of your budget (for example, if you want to budget $1000.00, enter "
                + "'1000.00'): $");
        BigDecimal budgetAmount = input.nextBigDecimal();
        budget = new Budget(budgetName, budgetAmount);
        account.addBudget(budget);
        System.out.println("Budget successfully created. View it below.");
        displayEverythingEmptyCategoriesAndTransactions();
    }

    // MODIFIES: this
    // EFFECTS: creates and adds category to a budget
    private void createAndAddCategory() {
        System.out.print("Enter the name of the category: ");
        String categoryName = input.nextLine();
        category = new Category(categoryName);
        if (!budget.addCategory(category)) {
            System.out.println("The category you entered already exists. You cannot add duplicate categories.");
        } else {
            System.out.println("Category successfully added. View it and the rest of your budget below.");
            displayEverythingEmptyCategoriesAndTransactions();
        }
    }

    // MODIFIES: this
    // EFFECTS: creates and adds transaction to a category, and formats the given transaction date
    private void createAndAddTransaction() {
        System.out.print("Enter the name of the transaction: ");
        String transactionName = input.nextLine();
        System.out.print("Enter the cost of the transaction: $");
        BigDecimal transactionCost = input.nextBigDecimal();
        System.out.print("Enter the date of the transaction in the format DD-MM-YYYY: ");
        input.nextLine();
        String transactionDate = input.nextLine();
        transaction = new Transaction(transactionName, transactionCost, transactionDate);
        while (!validateTransactionDate(transactionDate)) {
            System.out.println("Invalid date. Try again.");
            System.out.print("Enter the date of the transaction in the format DD-MM-YYYY: ");
            transactionDate = input.nextLine();
        }
        category.addTransaction(transaction);
        budget.calculateAmountRemaining();
        System.out.println("Transaction successfully added. View it and the rest of your budget below.");
        displayTransaction();
    }

    // MODIFIES: this
    // EFFECTS: displays everything but categories and transactions are empty,
    public void displayEverythingEmptyCategoriesAndTransactions() {
        if (budget.getCategoriesToDisplay().size() == 0) {
            displayBudget();
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("|                                    Categories                                    |");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("| You have no categories. To add a category, enter '" + ADD_CATEGORY_COMMAND
                    + "'.                 |");
            System.out.println("------------------------------------------------------------------------------------"
                    + "");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("|                                   Transactions                                   |");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("| You have no transactions. To add transactions, first add a category.             |");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("------------------------------------------------------------------------------------");
            appendViewOptionsMenuInstruction();
        } else {
            displayEverythingEmptyTransactions();
        }
    }

    // MODIFIES: this
    // EFFECTS: displays everything but transactions are empty
    public void displayEverythingEmptyTransactions() {
        if (transactionsIsEmpty()) {
            displayCategory();
            System.out.println("-----------------------------------------------------------------------------------"
                    + "-");
            System.out.println("|                                   Transactions                                  "
                    + " |");
            System.out.println("----------------------------------------------------------------------------------"
                    + "--");
            System.out.println("| You have no transactions. To add a transaction, enter '"
                    + ADD_TRANSACTION_COMMAND + "'.         |");
            System.out.println("------------------------------------------------------------------------------------");
            appendCategoryInstructions();
            appendViewOptionsMenuInstruction();
        } else {
            displayTransaction();
        }
    }

    // EFFECTS: returns true if list of transactions is empty, false otherwise
    public boolean transactionsIsEmpty() {
        boolean noTransactions = false;
        int numCategoryTransactions = 0;
        for (Category nextCategory : budget.getCategoriesToDisplay()) {
            numCategoryTransactions += nextCategory.getTransactions().size();
        }
        if (numCategoryTransactions == 0) {
            noTransactions = true;
        }
        return noTransactions;
    }

    // MODIFIES: this
    // EFFECTS: displays the budget in a table with columns for name, amount, spent, remaining and date created
    public void displayBudget() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("|                                      Budget                                      |");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.format("%-16s %-16s %-16s %-16s %-16s", "| Name", "| Amount", "| Spent",
                "| Remaining", "| Date Created |");
        System.out.println("\n------------------------------------------------------------------------------------");
        System.out.format("%-16s %-16s %-16s %-16s %-16s", "| ", "| ", "| ", "| ", "| " + "             |");
        System.out.format("\n%-16s %-16s %-16s %-16s %-16s", "| " + budget.getName(),
                "| $" + budget.getAmount(), "| $" + budget.getAmountSpent(), "| $"
                        + budget.getAmountRemaining(), "| "
                        + formatBudgetStartingDate(budget.formatStartingDate()));
        System.out.println("\n------------------------------------------------------------------------------------");
    }

    // MODIFIES: this
    // EFFECTS: displays the category in a table with the list of transactions
    public void displayCategory() {
        displayBudget();
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("|                                    Categories                                    |");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.format("%-40s %-40s", "| Name", "| Spent" + "                                   |");
        System.out.println("\n------------------------------------------------------------------------------------");
        for (Category nextCategory : budget.getCategoriesToDisplay()) {
            System.out.format("%-40s %-40s", "| ", "| " + "                                        |");
            System.out.format("\n%-40s %-40s", "| " + nextCategory.getName(), "| $"
                    + formatCategoryAmountSpent(nextCategory.getAmountSpent()));
            System.out.println("\n---------------------------------------------------------------------------------"
                    + "---");
        }
    }

    // MODIFIES: this
    // EFFECTS: displays the transaction in a table with the name, cost and date
    public void displayTransaction() {
        displayCategory();
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("|                                   Transactions                                   |");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.format("%-16s %-24s %-20s %-20s", "| Category", "| Transaction Name", "| Transaction Cost",
                "| Transaction Date  |");
        System.out.println("\n------------------------------------------------------------------------------------");
        for (Category nextCategory : budget.getCategoriesToDisplay()) {
            for (Transaction nextTransaction : nextCategory.getTransactions()) {
                System.out.format("%-16s %-24s %-20s %-20s", "| ", "| ", "| ", "| "
                        + "                  |");
                System.out.format("\n%-16s %-24s %-20s %-20s", "| " + nextCategory.getName(), "| "
                        + nextTransaction.getName(), "| $" + nextTransaction.getCost(), "| "
                        + formatTransactionDate(nextTransaction.getDate()));
                System.out.println("\n---------------------------------------------------------------------------------"
                        + "---");
            }
        }
        appendCategoryInstructions();
        appendTransactionInstructions();
        appendViewOptionsMenuInstruction();
    }

    // MODIFIES: this
    // EFFECTS: adds the instructions to view the options menu
    public void appendViewOptionsMenuInstruction() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("| To view the options menu, enter '" + MENU_COMMAND
                + "'.                                          |");
        System.out.println("------------------------------------------------------------------------------------");
    }

    // MODIFIES: this
    // EFFECTS: adds the instructions to add or delete a category
    public void appendCategoryInstructions() {
        if (category == null) {
            System.out.println("| SELECTED CATEGORY: No category selected. To select one, enter the category name. |");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("| To add a category, enter '" + ADD_CATEGORY_COMMAND
                    + "'.                                         |");
        } else {
            System.out.format("%-56s %-48s", "| SELECTED CATEGORY: '" + category.getName() + "'",
                    "                          |");
            System.out.println("\n| To add a category, enter '" + ADD_CATEGORY_COMMAND
                    + "'.                                         |");
            System.out.println("| To delete a category, enter 'delete category' and the category name.             |");
            System.out.println("| To change the selected category, enter the category name.                        |");
            System.out.println("------------------------------------------------------------------------------------");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds the instructions to add or delete a transaction
    public void appendTransactionInstructions() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("| To add a transaction, enter '" + ADD_TRANSACTION_COMMAND
                + "'.                                   |");
        System.out.println("| To delete a transaction, enter 'delete transaction' and the transaction name.    |");
        System.out.println("------------------------------------------------------------------------------------");
    }

    @Override
    // MODIFIES: this
    // EFFECTS: formats the given amount spent in the category depending on the value and returns it in the table
    public String formatCategoryAmountSpent(BigDecimal categoryAmountSpent) {
        if (categoryAmountSpent.compareTo(ten) < 0) {
            formattedCategoryAmountSpent = categoryAmountSpent + "                                   |";
        } else if (categoryAmountSpent.compareTo(oneHundred) < 0) {
            formattedCategoryAmountSpent = categoryAmountSpent + "                                  |";
        } else if (categoryAmountSpent.compareTo(oneThousand) < 0) {
            formattedCategoryAmountSpent = categoryAmountSpent + "                                 |";
        } else if (categoryAmountSpent.compareTo(tenThousand) < 0) {
            formattedCategoryAmountSpent = categoryAmountSpent + "                                |";
        } else if (categoryAmountSpent.compareTo(oneHundredThousand) < 0) {
            formattedCategoryAmountSpent = categoryAmountSpent + "                               |";
        } else if (categoryAmountSpent.compareTo(oneMillion) < 0) {
            formattedCategoryAmountSpent = categoryAmountSpent + "                              |";
        }
        return formattedCategoryAmountSpent;
    }

    // MODIFIES: this
    // EFFECTS: formats the budget starting date depending on the value and returns it in the table
    public String formatBudgetStartingDate(String budgetStartingDate) {
        return formatDate(budgetStartingDate, "     |", "    |",
                "   |");
    }

    // MODIFIES: this
    // EFFECTS: formats the given transaction date depending on the value and returns it in the table
    public String formatTransactionDate(String transactionDate) {
        return formatDate(transactionDate, "          |",
                "         |", "        |");
    }

    // EFFECTS: returns true if the given input is a valid date, false otherwise
    public boolean validateTransactionDate(String transactionDate) {
        boolean isValid = true;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d-M-uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            dateFormatter.parse(transactionDate);
            transaction.setDate(transactionDate);
        } catch (DateTimeParseException exception) {
            isValid = false;
        }
        return isValid;
    }

    // MODIFIES: this
    // EFFECTS: saves changes to account to file
    private void saveAccount() {
        try {
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();
            System.out.println("Changes to your account were successfully saved to " + JSON_STORE);
        } catch (FileNotFoundException exception) {
            System.out.println("Unable to save changes to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: returns true and loads account if the given username and password match the account from file,
    // false otherwise
    private boolean loadAccount(String username, String password) {
        boolean isLoaded = false;
        try {
            account = jsonReader.read();
            if (username.equals(account.getUsername()) && password.equals(account.getPassword())) {
                isLoaded = true;
                System.out.println("You successfully signed in to your account loaded from '" + JSON_STORE);
                System.out.println("View your account below.");
                for (Budget nextBudget : account.getBudgets()) {
                    nextBudget.calculateAmountRemaining();
                }
                displayAccount();
            }
        } catch (IOException exception) {
            System.out.println("Unable to load account from file " + JSON_STORE);
        }
        return isLoaded;
    }
}
