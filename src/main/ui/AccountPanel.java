package ui;

import model.Account;
import model.exceptions.*;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Represents the account panel.
 */
public class AccountPanel extends JPanel implements ColourRepository, FontRepository {
    private static final String JSON_STORE = "./data/accounts.json";
    private static final Dimension PANEL_DIMENSIONS = new Dimension(1060, 150);
    private static final Dimension BUTTON_DIMENSIONS = new Dimension(65, 30);

    private Account account;
    private JPanel mainPanel;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private RoundedPanel namePanel;
    private RoundedPanel usernamePanel;
    private RoundedPanel passwordPanel;
    private RoundedPanel deleteAccountPanel;
    private JButton buttonToSaveName;
    private JButton buttonToSaveUsername;
    private JButton buttonToSavePassword;
    private JButton buttonToDeleteAccount;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField usernameField;
    private JTextField currentPasswordField;
    private JTextField newPasswordField;
    private JTextField confirmNewPasswordField;
    private JTextField passwordField;
    private ResettableDialog dialogToDeleteAccount;
    private JOptionPane optionPaneToDeleteAccount;

    /**
     * Creates a new account panel with the specified account.
     *
     * @param account the account the user is signed in to
     * @param mainPanel the main panel
     */
    public AccountPanel(Account account, JPanel mainPanel) {
        this.account = account;
        this.mainPanel = mainPanel;
        setLayout(new GridBagLayout());
        initializeJson();
        initializeNamePanel();
        initializeUsernamePanel();
        initializePasswordPanel();
        initializeDeleteAccountPanel();
    }

    /**
     * Initializes the JSON reader and the JSON writer.
     */
    private void initializeJson() {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    /**
     * Initializes the name panel and adds it to this account panel.
     */
    private void initializeNamePanel() {
        JLabel nameLabel = new JLabel("Name");
        namePanel = new RoundedPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        nameLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        nameLabel.setForeground(Color.WHITE);
        namePanel.setPreferredSize(PANEL_DIMENSIONS);
        namePanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        namePanel.setLayout(new GridBagLayout());
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-85, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        namePanel.add(nameLabel, gridBagConstraints);
        initializeSeparatorForNamePanel();
        initializeFirstNameField();
        initializeLastNameField();
        initializeButtonToSaveName();
        gridBagConstraints.insets = new Insets(-15, 0, 38, 0);
        gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
        add(namePanel, gridBagConstraints);
    }

    /**
     * Initializes the username panel and adds it to this account panel.
     */
    private void initializeUsernamePanel() {
        JLabel usernameLabel = new JLabel("Username");
        usernamePanel = new RoundedPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        usernameLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        usernameLabel.setForeground(Color.WHITE);
        usernamePanel.setPreferredSize(PANEL_DIMENSIONS);
        usernamePanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        usernamePanel.setLayout(new GridBagLayout());
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-83, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        usernamePanel.add(usernameLabel, gridBagConstraints);
        initializeSeparatorForUsernamePanel();
        initializeUsernameField();
        initializeButtonToSaveUsername();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, 38, 0);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        add(usernamePanel, gridBagConstraints);
    }

    /**
     * Initializes the password panel and adds it to this account panel.
     */
    private void initializePasswordPanel() {
        JLabel passwordLabel = new JLabel("Password");
        passwordPanel = new RoundedPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        passwordLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        passwordLabel.setForeground(Color.WHITE);
        passwordPanel.setPreferredSize(PANEL_DIMENSIONS);
        passwordPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        passwordPanel.setLayout(new GridBagLayout());
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-83, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        passwordPanel.add(passwordLabel, gridBagConstraints);
        initializeSeparatorForPasswordPanel();
        initializeCurrentPasswordField();
        initializeNewPasswordField();
        initializeConfirmNewPasswordField();
        initializeButtonToSavePassword();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, 38, 0);
        gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
        add(passwordPanel, gridBagConstraints);
    }

    /**
     * Initializes the delete account panel and adds it to this account panel.
     */
    private void initializeDeleteAccountPanel() {
        JLabel deleteAccountLabel = new JLabel("Delete Account");
        deleteAccountPanel = new RoundedPanel();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        deleteAccountLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        deleteAccountLabel.setForeground(Color.WHITE);
        deleteAccountPanel.setPreferredSize(new Dimension(1060, 117));
        deleteAccountPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        deleteAccountPanel.setLayout(new GridBagLayout());
        initializeSeparatorForDeleteAccountPanel();
        initializeWarningLabelForDeleteAccountPanel();
        initializeButtonToDeleteAccount();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-30, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        deleteAccountPanel.add(deleteAccountLabel, gridBagConstraints);
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
        add(deleteAccountPanel, gridBagConstraints);
    }

    /**
     * Initializes the warning label and adds it to the delete account panel.
     */
    private void initializeWarningLabelForDeleteAccountPanel() {
        JLabel warningLabel = new JLabel("This will permanently delete your account and all of its data."
                + " You will not be able to recover this account.");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        warningLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        warningLabel.setForeground(Color.WHITE);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -47, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        deleteAccountPanel.add(warningLabel, gridBagConstraints);
    }

    /**
     * Initializes the button the delete account and adds it to the delete account panel.
     */
    private void initializeButtonToDeleteAccount() {
        buttonToDeleteAccount = new JButton("Delete Account");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        initializeOptionPaneToDeleteAccount();
        initializeDialogToDeleteAccount();
        buttonToDeleteAccount.setPreferredSize(new Dimension(130, 30));
        buttonToDeleteAccount.setBorderPainted(false);
        buttonToDeleteAccount.setBackground(ERROR_COLOUR);
        buttonToDeleteAccount.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        buttonToDeleteAccount.setForeground(Color.WHITE);
        buttonToDeleteAccount.addActionListener(event -> {
            buttonToDeleteAccount.putClientProperty("JComponent.outline", ERROR_BORDER_COLOUR);
            buttonToDeleteAccount.setBorderPainted(true);
            dialogToDeleteAccount.setVisible(true);
        });
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-28, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        deleteAccountPanel.add(buttonToDeleteAccount, gridBagConstraints);
    }

    /**
     * Initializes the dialog to delete account.
     */
    private void initializeDialogToDeleteAccount() {
        List<JTextField> deleteAccountFields = Collections.singletonList(passwordField);
        dialogToDeleteAccount = new ResettableDialog("bdgtr", deleteAccountFields);
        dialogToDeleteAccount.setContentPane(optionPaneToDeleteAccount);
        dialogToDeleteAccount.pack();
        dialogToDeleteAccount.setLocationRelativeTo(null);
    }

    /**
     * Initializes the option pane to delete account.
     */
    private void initializeOptionPaneToDeleteAccount() {
        passwordField = new JPasswordField(12);
        passwordField.putClientProperty("JTextField.placeholderText", "Password");
        passwordField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        JLabel passwordLabel = new JLabel("Enter your password to confirm the deletion of your account:");
        JPanel deleteAccountLabelsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel deleteAccountFieldsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JPanel panelToDeleteAccount = new JPanel(new BorderLayout(10, 10));
        optionPaneToDeleteAccount = new JOptionPane(panelToDeleteAccount, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
        passwordLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        passwordLabel.setForeground(Color.WHITE);
        deleteAccountLabelsPanel.add(passwordLabel);
        deleteAccountFieldsPanel.add(passwordField);
        panelToDeleteAccount.add(deleteAccountLabelsPanel, BorderLayout.LINE_START);
        panelToDeleteAccount.add(deleteAccountFieldsPanel, BorderLayout.CENTER);
        addPropertyChangeListenerToOptionPaneToDeleteAccount();
    }

    /**
     * Adds a property change listener to the option pane to delete account.
     */
    private void addPropertyChangeListenerToOptionPaneToDeleteAccount() {
        optionPaneToDeleteAccount.addPropertyChangeListener(event -> {
            if (dialogToDeleteAccount.isVisible() && (event.getSource().equals(optionPaneToDeleteAccount))
                    && (event.getPropertyName().equals(JOptionPane.VALUE_PROPERTY))) {
                Object option = optionPaneToDeleteAccount.getValue();
                if (option.equals(JOptionPane.UNINITIALIZED_VALUE)) {
                    return;
                }
                optionPaneToDeleteAccount.setValue(JOptionPane.UNINITIALIZED_VALUE);
                if (option.equals(JOptionPane.OK_OPTION)) {
                    if (passwordField.getText().equals(account.getPassword())) {
                        deleteAccount();
                        dialogToDeleteAccount.setVisible(false);
                    }
                } else if (option.equals(JOptionPane.CANCEL_OPTION)) {
                    dialogToDeleteAccount.setVisible(false);
                }
                buttonToDeleteAccount.setBorderPainted(false);
            }
        });
    }

    /**
     * Deletes the account.
     */
    private void deleteAccount() {
        try {
            jsonWriter.open();
            jsonWriter.delete(account, null);
            jsonWriter.close();
            JFrame mainWindow = (JFrame) getTopLevelAncestor();
            mainWindow.setJMenuBar(null);
            SwingUtilities.getWindowAncestor(mainPanel).add(new EntryPanel());
            SwingUtilities.getWindowAncestor(mainPanel).revalidate();
            SwingUtilities.getWindowAncestor(mainPanel).repaint();
            SwingUtilities.getWindowAncestor(mainPanel).remove(mainPanel);
            javax.swing.FocusManager.getCurrentManager().getActiveWindow().revalidate();
            javax.swing.FocusManager.getCurrentManager().getActiveWindow().repaint();
            JOptionPane.showMessageDialog(this, "Your account has been deleted.", "bdgtr",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Initializes the first name field and adds it to the name panel.
     */
    private void initializeFirstNameField() {
        JLabel firstNameLabel = new JLabel("First Name");
        firstNameField = new JTextField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        firstNameLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        firstNameLabel.setForeground(Color.WHITE);
        firstNameField.putClientProperty("JTextField.placeholderText", account.getFirstName());
        firstNameField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        addListenerToFirstNameField();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 3, -30, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        namePanel.add(firstNameLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -87, 0);
        namePanel.add(firstNameField, gridBagConstraints);
    }

    /**
     * Adds a listener to the first name field.
     * Enables the button to save name if the first name field is not empty.
     */
    public void addListenerToFirstNameField() {
        firstNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                buttonToSaveName.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                if (firstNameField.getText().isEmpty()) {
                    buttonToSaveName.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                buttonToSaveName.setEnabled(false);
            }
        });
    }

    /**
     * Initializes the last name field and adds it to the name panel.
     */
    private void initializeLastNameField() {
        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameField = new JTextField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        lastNameLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        lastNameLabel.setForeground(Color.WHITE);
        lastNameField.putClientProperty("JTextField.placeholderText", account.getLastName());
        lastNameField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        addListenerToLastNameField();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -187, -30, 0);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        namePanel.add(lastNameLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -87, 0);
        namePanel.add(lastNameField, gridBagConstraints);
    }

    /**
     * Adds a listener to the last name field.
     * Enables the button to save name if the last name field is not empty.
     */
    public void addListenerToLastNameField() {
        lastNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                buttonToSaveName.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                if (lastNameField.getText().isEmpty()) {
                    buttonToSaveName.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                buttonToSaveName.setEnabled(false);
            }
        });
    }

    /**
     * Initializes the button to save name and adds it to the name panel.
     */
    private void initializeButtonToSaveName() {
        buttonToSaveName = new JButton("Save");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        buttonToSaveName.setPreferredSize(BUTTON_DIMENSIONS);
        buttonToSaveName.setBorderPainted(false);
        buttonToSaveName.setBackground(ACCENT_COLOUR);
        buttonToSaveName.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        buttonToSaveName.setForeground(Color.WHITE);
        buttonToSaveName.setEnabled(false);
        addActionListenerToButtonToSaveName();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-85, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        namePanel.add(buttonToSaveName, gridBagConstraints);
    }

    /**
     * Initializes the button to save username and adds it to the username panel.
     */
    private void initializeButtonToSaveUsername() {
        buttonToSaveUsername = new JButton("Save");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        buttonToSaveUsername.setPreferredSize(BUTTON_DIMENSIONS);
        buttonToSaveUsername.setBorderPainted(false);
        buttonToSaveUsername.setBackground(ACCENT_COLOUR);
        buttonToSaveUsername.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        buttonToSaveUsername.setForeground(Color.WHITE);
        buttonToSaveUsername.setEnabled(false);
        addActionListenerToButtonToSaveUsername();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-82, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        usernamePanel.add(buttonToSaveUsername, gridBagConstraints);
    }

    /**
     * Initializes the button to save password and adds it to the password panel.
     */
    private void initializeButtonToSavePassword() {
        buttonToSavePassword = new JButton("Save");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        buttonToSavePassword.setPreferredSize(BUTTON_DIMENSIONS);
        buttonToSavePassword.setBorderPainted(false);
        buttonToSavePassword.setBackground(ACCENT_COLOUR);
        buttonToSavePassword.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        buttonToSavePassword.setForeground(Color.WHITE);
        buttonToSavePassword.setEnabled(false);
        addActionListenerToButtonToSavePassword();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(-82, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        passwordPanel.add(buttonToSavePassword, gridBagConstraints);
    }

    /**
     * Adds an action listener to the button to save name.
     */
    private void addActionListenerToButtonToSaveName() {
        buttonToSaveName.addActionListener(event -> {
            buttonToSaveName.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
            buttonToSaveName.setBorderPainted(true);
            if (!firstNameField.getText().isEmpty() && !lastNameField.getText().isEmpty()) {
                changeFullName();
            } else if (!firstNameField.getText().isEmpty()) {
                changeFirstName();
            } else if (!lastNameField.getText().isEmpty()) {
                changeLastName();
            } else {
                JOptionPane.showMessageDialog(null,
                        "There are no changes to save.", "bdgtr", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    /**
     * Adds an action listener to the button to save username.
     */
    private void addActionListenerToButtonToSaveUsername() {
        buttonToSaveUsername.addActionListener(event -> {
            buttonToSaveUsername.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
            buttonToSaveUsername.setBorderPainted(true);
            if (!usernameField.getText().isEmpty()) {
                try {
                    jsonReader.read(usernameField.getText());
                    usernameField.putClientProperty("JComponent.outline", "error");
                    JOptionPane.showMessageDialog(this, "Username has already been taken.",
                            "bdgtr", JOptionPane.ERROR_MESSAGE);
                } catch (IOException | EmptyFirstNameException | EmptyLastNameException | EmptyUsernameException
                        | EmptyPasswordException | EmptyNameException | NegativeAmountException | ZeroAmountException
                        | DuplicateBudgetException | DuplicateCategoryException exception) {
                    exception.printStackTrace();
                } catch (JSONException jsonException) {
                    changeUsername();
                }
            }
        });
    }

    /**
     * Adds an action listener to the button to save password.
     */
    private void addActionListenerToButtonToSavePassword() {
        buttonToSavePassword.addActionListener(event -> {
            buttonToSavePassword.putClientProperty("JComponent.outline", ACCENT_BORDER_COLOUR);
            buttonToSavePassword.setBorderPainted(true);
            checkPasswordStepOne();
            refresh(this);
        });
    }

    /**
     * Checks whether the current password matches the account's password.
     */
    private void checkPasswordStepOne() {
        if (currentPasswordField.getText().equals(account.getPassword())) {
            currentPasswordField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
            checkPasswordStepTwo();
        } else {
            currentPasswordField.putClientProperty("JComponent.outline", "error");
            JOptionPane.showMessageDialog(this, "Incorrect password.",
                    "bdgtr", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks whether the new password matches the confirm new password.
     */
    private void checkPasswordStepTwo() {
        if (newPasswordField.getText().equals(confirmNewPasswordField.getText())) {
            newPasswordField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
            confirmNewPasswordField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
            checkPasswordStepThree();
        } else {
            newPasswordField.putClientProperty("JComponent.outline", "error");
            confirmNewPasswordField.putClientProperty("JComponent.outline", "error");
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                    "bdgtr", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks whether the new password matches the current password.
     */
    private void checkPasswordStepThree() {
        if (!newPasswordField.getText().equals(currentPasswordField.getText())) {
            changePassword();
        } else {
            newPasswordField.putClientProperty("JComponent.outline", "error");
            JOptionPane.showMessageDialog(this,
                    "Your new password cannot be the same as your current password.",
                    "bdgtr", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Changes the full name.
     */
    private void changeFullName() {
        try {
            account.setFirstName(firstNameField.getText());
            account.setLastName(lastNameField.getText());
            firstNameField.putClientProperty("JTextField.placeholderText", firstNameField.getText());
            lastNameField.putClientProperty("JTextField.placeholderText", lastNameField.getText());
            firstNameField.setText(null);
            lastNameField.setText(null);
            refresh(firstNameField);
            refresh(lastNameField);
            saveActionPerformed();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Changes the first name.
     */
    private void changeFirstName() {
        try {
            account.setFirstName(firstNameField.getText());
            firstNameField.putClientProperty("JTextField.placeholderText", firstNameField.getText());
            firstNameField.setText(null);
            refresh(firstNameField);
            saveActionPerformed();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Changes the last name.
     */
    private void changeLastName() {
        try {
            account.setLastName(lastNameField.getText());
            lastNameField.putClientProperty("JTextField.placeholderText", lastNameField.getText());
            lastNameField.setText(null);
            refresh(lastNameField);
            saveActionPerformed();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Changes the username.
     */
    private void changeUsername() {
        String oldUsername = account.getUsername();
        account.setUsername(usernameField.getText());
        usernameField.putClientProperty("JTextField.placeholderText", usernameField.getText());
        usernameField.setText(null);
        refresh(usernameField);
        try {
            jsonWriter.open();
            jsonWriter.delete(account, oldUsername);
            jsonWriter.close();
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null,
                    "Changes have been successfully saved. They will be applied the next time you sign in.",
                    "bdgtr", JOptionPane.INFORMATION_MESSAGE);
            buttonToSaveName.setBorderPainted(false);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Changes the password.
     */
    private void changePassword() {
        try {
            account.setPassword(newPasswordField.getText());
            saveActionPerformed();
            currentPasswordField.putClientProperty("JComponent.outline", null);
            newPasswordField.putClientProperty("JComponent.outline", null);
            confirmNewPasswordField.putClientProperty("JComponent.outline", null);
            currentPasswordField.setText(null);
            newPasswordField.setText(null);
            confirmNewPasswordField.setText(null);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Initializes the username field and adds it to the username panel.
     */
    private void initializeUsernameField() {
        JLabel usernameLabel = new JLabel("Username");
        usernameField = new JTextField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        usernameLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        usernameLabel.setForeground(Color.WHITE);
        usernameField.putClientProperty("JTextField.placeholderText", account.getUsername());
        usernameField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        addListenerToUsernameField();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 3, -30, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        usernamePanel.add(usernameLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -87, 0);
        usernamePanel.add(usernameField, gridBagConstraints);
    }

    /**
     * Adds a listener to the username field.
     * Enables the button to save username if the username field is not empty.
     */
    public void addListenerToUsernameField() {
        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                buttonToSaveUsername.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                if (usernameField.getText().isEmpty()) {
                    buttonToSaveUsername.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                buttonToSaveUsername.setEnabled(false);
            }
        });
    }

    /**
     * Initializes the current password field and adds it to the password panel.
     */
    private void initializeCurrentPasswordField() {
        JLabel currentPasswordLabel = new JLabel("Current Password");
        currentPasswordField = new JPasswordField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        currentPasswordLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        currentPasswordLabel.setForeground(Color.WHITE);
        currentPasswordField.putClientProperty("JTextField.placeholderText", "Current Password");
        currentPasswordField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        addListenerToCurrentPasswordField();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 3, -30, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        passwordPanel.add(currentPasswordLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -87, 0);
        passwordPanel.add(currentPasswordField, gridBagConstraints);
    }

    /**
     * Adds a listener to the current password field.
     */
    public void addListenerToCurrentPasswordField() {
        currentPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                if (!currentPasswordField.getText().isEmpty() && !newPasswordField.getText().isEmpty()
                        && !confirmNewPasswordField.getText().isEmpty()) {
                    buttonToSavePassword.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                if (currentPasswordField.getText().isEmpty()) {
                    buttonToSavePassword.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                buttonToSavePassword.setEnabled(false);
            }
        });
    }

    /**
     * Initializes the new password field and adds it to the password panel.
     */
    private void initializeNewPasswordField() {
        JLabel newPasswordLabel = new JLabel("New Password");
        newPasswordField = new JPasswordField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        newPasswordLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        newPasswordLabel.setForeground(Color.WHITE);
        newPasswordField.putClientProperty("JTextField.placeholderText", "New Password");
        newPasswordField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        addListenerToNewPasswordField();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -163, -30, 0);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        passwordPanel.add(newPasswordLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, -87, 0);
        passwordPanel.add(newPasswordField, gridBagConstraints);
    }

    /**
     * Adds a listener to the new password field.
     */
    public void addListenerToNewPasswordField() {
        newPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                if (!currentPasswordField.getText().isEmpty() && !newPasswordField.getText().isEmpty()
                        && !confirmNewPasswordField.getText().isEmpty()) {
                    buttonToSavePassword.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                if (newPasswordField.getText().isEmpty()) {
                    buttonToSavePassword.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                buttonToSavePassword.setEnabled(false);
            }
        });
    }

    /**
     * Initializes the confirm new password field and adds it to the password panel.
     */
    private void initializeConfirmNewPasswordField() {
        JLabel confirmNewPasswordLabel = new JLabel("Confirm New Password");
        confirmNewPasswordField = new JPasswordField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        confirmNewPasswordLabel.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        confirmNewPasswordLabel.setForeground(Color.WHITE);
        confirmNewPasswordField.putClientProperty("JTextField.placeholderText", "Confirm New Password");
        confirmNewPasswordField.setFont(HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        addListenerToConfirmNewPasswordField();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, -30, -650);
        passwordPanel.add(confirmNewPasswordLabel, gridBagConstraints);
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(0, 0, -87, 0);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        passwordPanel.add(confirmNewPasswordField, gridBagConstraints);
    }

    /**
     * Adds a listener to the confirm new password field.
     */
    public void addListenerToConfirmNewPasswordField() {
        confirmNewPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                if (!currentPasswordField.getText().isEmpty() && !newPasswordField.getText().isEmpty()
                        && !confirmNewPasswordField.getText().isEmpty()) {
                    buttonToSavePassword.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                if (confirmNewPasswordField.getText().isEmpty()) {
                    buttonToSavePassword.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                buttonToSavePassword.setEnabled(false);
            }
        });
    }

    /**
     * Initializes the separator for the name panel.
     */
    private void initializeSeparatorForNamePanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(-20, 0, 0, 0);
        gridBagConstraints.weightx = 1;
        namePanel.add(separator, gridBagConstraints);
    }

    /**
     * Initializes the separator for the username panel.
     */
    private void initializeSeparatorForUsernamePanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(-17, 0, 0, 0);
        gridBagConstraints.weightx = 1;
        usernamePanel.add(separator, gridBagConstraints);
    }

    /**
     * Initializes the separator for the password panel.
     */
    private void initializeSeparatorForPasswordPanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(-17, 0, 0, 0);
        gridBagConstraints.weightx = 1;
        passwordPanel.add(separator, gridBagConstraints);
    }

    /**
     * Initializes the separator for the delete account panel.
     */
    private void initializeSeparatorForDeleteAccountPanel() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(17, 0, 0, 0);
        gridBagConstraints.weightx = 1;
        deleteAccountPanel.add(separator, gridBagConstraints);
    }

    /**
     * Saves changes and shows the "Changes have been successfully saved.
     * They will be applied the next time you sign in." message dialog.
     *
     * @throws IOException if an error occurs reading data from file
     */
    private void saveActionPerformed() throws IOException {
        jsonWriter.open();
        jsonWriter.write(account);
        jsonWriter.close();
        JOptionPane.showMessageDialog(null,
                "Changes have been successfully saved. They will be applied the next time you sign in.",
                "bdgtr", JOptionPane.INFORMATION_MESSAGE);
        buttonToSaveName.setBorderPainted(false);
    }

    /**
     * Refreshes the specified component.
     *
     * @param component the component to be refreshed
     */
    private void refresh(Component component) {
        component.revalidate();
        component.repaint();
    }
}
