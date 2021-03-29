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
 * Represents the graphical user interface for the BDGTR application.
 */
public class Bdgtr extends JFrame {
    private static final String JSON_STORE = "./data/accounts.json";

    private Account account;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private LandingPanel landingPanel;

    /**
     * Creates the main window.
     *
     * @throws IOException if an error occurs reading data from the file
     */
    public Bdgtr() throws IOException {
        super("BDGTR");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    handleClosing();
                } catch (IOException | EmptyUsernameException | EmptyPasswordException | EmptyNameException
                        | NegativeAmountException | DuplicateBudgetException | DuplicateCategoryException
                        | NegativeCostException exception) {
                    exception.printStackTrace();
                }
            }
        });
        initializeJson();
        initializeComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initializes the JsonReader and JsonWriter.
     */
    private void initializeJson() {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    /**
     * Initializes all components.
     *
     * @throws IOException if an error occurs reading data from the file
     */
    private void initializeComponents() throws IOException {
        landingPanel = new LandingPanel();
        add(landingPanel);
    }

    /**
     * Gives the user the option to save their changes to file if they have unsaved changes,
     * dispose the window otherwise.
     *
     * @throws IOException if an error occurs reading data from the file
     */
    private void handleClosing() throws IOException, EmptyUsernameException, EmptyPasswordException,
            EmptyNameException, NegativeAmountException, DuplicateBudgetException, DuplicateCategoryException,
            NegativeCostException {
        if (hasUnsavedChanges()) {
            int answer = showClosingOptionPane();
            switch (answer) {
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

    /**
     * Check whether the user has any unsaved changes.
     *
     * @return true if the user has any unsaved changes, false otherwise
     * @throws IOException if an error occurs reading data from the file
     */
    private boolean hasUnsavedChanges() throws IOException, EmptyUsernameException, EmptyPasswordException,
            EmptyNameException, NegativeAmountException, DuplicateBudgetException, DuplicateCategoryException,
            NegativeCostException {
        boolean hasUnsavedChanges = false;
        if (account != null) {
            Account unsavedAccount = jsonReader.read(getAccount().getUsername());
            if (!unsavedAccount.equals(getAccount())) {
                hasUnsavedChanges = true;
            }
        }
        return hasUnsavedChanges;
    }

    /**
     * Gets the Account the user is signed in to.
     *
     * @return the Account the user is signed in to
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Shows the closing option pane.
     *
     * @return an integer from 0 to 2 with 0 indicating "Yes", 1 indicating "No" and 2 indicating "Cancel"
     */
    private int showClosingOptionPane() {
        String[] buttonLabels = new String[] {"Yes", "No", "Cancel"};
        String defaultOption = buttonLabels[0];
        return JOptionPane.showOptionDialog(this, "Want to save your changes to '" + JSON_STORE
                        + "?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
                buttonLabels, defaultOption);
    }
}
