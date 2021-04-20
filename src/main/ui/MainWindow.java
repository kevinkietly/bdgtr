package ui;

import com.apple.eawt.Application;
import model.Account;
import model.exceptions.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * Represents the main window.
 */
public class MainWindow extends JFrame {
    private static final String JSON_STORE = "./data/accounts.json";

    private Account account;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private EntryPanel entryPanel;

    /**
     * Creates a new main window.
     *
     * @throws IOException if an error occurs reading data from file
     */
    public MainWindow() throws IOException {
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
        Image image = ImageIO.read(new File("./icons/Bdgtr_Icon.png"));
        Application.getApplication().setDockIconImage(new ImageIcon(image).getImage());
        setIconImage(image);
    }

    /**
     * Initializes the JSON reader and the JSON writer.
     */
    private void initializeJson() {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    /**
     * Initializes the entry panel and adds it to this main window.
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
        String[] buttonLabels = {"Yes", "No", "Cancel"};
        String defaultOption = buttonLabels[0];
        return JOptionPane.showOptionDialog(this,
                "You have unsaved changes. Want to save your changes?", "bdgtr",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, buttonLabels, defaultOption);
    }

    /**
     * Check whether the user has unsaved changes.
     *
     * @return true if the user has unsaved changes, false otherwise
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
     * Provides the option to save changes to file if the user has unsaved changes, dispose the main window otherwise.
     *
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
