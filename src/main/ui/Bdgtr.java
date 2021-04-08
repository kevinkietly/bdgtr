package ui;

import model.Account;
import model.exceptions.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Represents the graphical user interface.
 */
public class Bdgtr extends JFrame {
    private static final String JSON_STORE = "./data/accounts.json";

    private Account account;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private EntryPanel entryPanel;

    /**
     * Creates a new frame.
     *
     * @throws IOException if an error occurs reading data from file
     */
    public Bdgtr() throws IOException {
        super("bdgtr");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    handleClosing();
                } catch (IOException | EmptyFirstNameException | EmptyLastNameException | EmptyUsernameException
                        | EmptyPasswordException | EmptyNameException | NegativeAmountException | ZeroAmountException
                        | DuplicateBudgetException | DuplicateCategoryException exception) {
                    exception.printStackTrace();
                }
            }
        });
        initializeJson();
        initializeEntryPanel();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initializes the JSON reader and the JSON writer.
     */
    private void initializeJson() {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    /**
     * Initializes the entry panel and adds it to this frame.
     *
     * @throws IOException if an error occurs reading data from file
     */
    private void initializeEntryPanel() throws IOException {
        entryPanel = new EntryPanel();
        add(entryPanel);
    }

    /**
     * Initializes the closing option pane and shows it.
     *
     * @return an integer from 0 to 2 in which 0 represents the "Yes" option, 1 represents the "No" option,
     * and 2 represents the "Cancel" option
     */
    private int initializeClosingOptionPane() {
        String[] buttonLabels = new String[] {"Yes", "No", "Cancel"};
        String defaultOption = buttonLabels[0];
        return JOptionPane.showOptionDialog(this,
                "You have unsaved changes. Want to save your changes?", "bdgtr",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, buttonLabels, defaultOption);
    }

    /**
     * Check whether there are unsaved changes.
     *
     * @return true if there are unsaved changes, false otherwise
     * @throws IOException if an error occurs reading data from file
     * @throws EmptyFirstNameException if the first name has length zero
     * @throws EmptyLastNameException if the last name has length zero
     * @throws EmptyUsernameException if the username has length zero
     * @throws EmptyPasswordException if the password has length zero
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     * @throws DuplicateBudgetException if the budget already exists in this account
     * @throws DuplicateCategoryException if the category already exists in this budget
     */
    private boolean hasUnsavedChanges() throws IOException, EmptyFirstNameException, EmptyLastNameException,
            EmptyUsernameException, EmptyPasswordException, EmptyNameException, NegativeAmountException,
            ZeroAmountException, DuplicateBudgetException, DuplicateCategoryException {
        boolean hasUnsavedChanges = false;
        if (entryPanel.getAccount() != null) {
            Account unsavedAccount = jsonReader.read(entryPanel.getAccount().getUsername());
            if (!unsavedAccount.equals(entryPanel.getAccount())) {
                hasUnsavedChanges = true;
            }
        }
        return hasUnsavedChanges;
    }

    /**
     * Provides the option to save changes to file if there are unsaved changes, dispose the frame otherwise.
     *
     * @throws IOException if file cannot be opened for writing
     * @throws EmptyFirstNameException if the first name has length zero
     * @throws EmptyLastNameException if the last name has length zero
     * @throws EmptyUsernameException if the username has length zero
     * @throws EmptyPasswordException if the password has length zero
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     * @throws DuplicateBudgetException if the budget already exists in this account
     * @throws DuplicateCategoryException if the category already exists in this budget
     */
    private void handleClosing() throws IOException, EmptyFirstNameException, EmptyLastNameException,
            EmptyUsernameException, EmptyPasswordException, EmptyNameException, NegativeAmountException,
            ZeroAmountException, DuplicateBudgetException, DuplicateCategoryException {
        if (hasUnsavedChanges()) {
            int option = initializeClosingOptionPane();
            switch (option) {
                case JOptionPane.YES_OPTION:
                    jsonWriter.open();
                    jsonWriter.write(account);
                    jsonWriter.close();
                    dispose();
                    break;
                case JOptionPane.NO_OPTION:
                    dispose();
                case JOptionPane.CANCEL_OPTION:
                    break;
            }
        } else {
            dispose();
        }
    }
}
