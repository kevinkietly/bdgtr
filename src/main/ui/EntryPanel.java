package ui;

import model.Account;
import model.exceptions.*;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the entry panel.
 */
public class EntryPanel extends JPanel implements FontRepository, ColourRepository, SoundRepository {
    private static final String JSON_STORE = "./data/accounts.json";
    private static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);
    private static final Insets USERNAME_FIELD_INSETS = new Insets(0, 0, 0, 235);
    private static final Insets PASSWORD_FIELD_INSETS = new Insets(0, 0, 0, 237);
    private static final Insets TEXT_FIELD_INSETS = new Insets(0, 100, 25, 100);
    private static final Insets BUTTON_INSETS = TEXT_FIELD_INSETS;
    private static final Dimension BUTTON_DIMENSIONS = new Dimension(313, 35);

    private Account account;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private JPanel signInPanel;
    private JPanel signUpPanel;
    private JPanel mainPanel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private List<JTextField> signUpFields;

    /**
     * Creates a new entry panel.
     *
     * @throws IOException if an error occurs reading data from file
     */
    public EntryPanel() throws IOException {
        setBorder(new EmptyBorder(100, 0, 100, 0));
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(1440, 847));
        initializeJson();
        initializeLogoPanel();
        initializeSeparator();
        initializeSignInPanel();
        signUpFields = new ArrayList<>();
    }

    /**
     * Initializes the JSON reader and the JSON writer.
     */
    private void initializeJson() {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    /**
     * Initializes the logo panel and adds it to this entry panel.
     *
     * @throws IOException if an error occurs reading data from file
     */
    private void initializeLogoPanel() throws IOException {
        JPanel logoPanel = new JPanel(new GridBagLayout());
        BufferedImage logoImage = ImageIO.read(new File("./images/Bdgtr_Logo.png"));
        ImageIcon logoIcon = new ImageIcon(logoImage);
        JLabel logoLabel = new JLabel(logoIcon);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 250, 0, 100);
        logoPanel.add(logoLabel, gridBagConstraints);
        gridBagConstraints.insets = ZERO_INSETS;
        add(logoPanel, gridBagConstraints);
    }

    /**
     * Initializes the separator and adds it to this entry panel.
     */
    private void initializeSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1;
        add(separator, gridBagConstraints);
    }

    /**
     * Initializes the sign in panel and adds it to this entry panel.
     */
    private void initializeSignInPanel() {
        signInPanel = new JPanel(new GridBagLayout());
        JLabel signInLabel = new JLabel("Sign In");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        signInLabel.setFont(HELVETICA_NEUE_LIGHT_HEADING_PLAIN);
        signInLabel.setForeground(Color.WHITE);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 220, 50, 220);
        signInPanel.add(signInLabel, gridBagConstraints);
        initializeSignInUsernameField();
        initializeSignInPasswordField();
        initializeRememberMeCheckBox();
        initializeForgotPasswordLabel();
        initializeSignInButton();
        initializeSignUpLabel();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = ZERO_INSETS;
        add(signInPanel, gridBagConstraints);
    }

    /**
     * Initializes the username field and adds it to the sign in panel.
     */
    private void initializeSignInUsernameField() {
        JLabel usernameLabel = new JLabel("Username");
        usernameField = new JTextField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        usernameLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        usernameLabel.setForeground(Color.WHITE);
        usernameField.putClientProperty("JTextField.placeholderText", "Username");
        usernameField.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        usernameField.setForeground(Color.WHITE);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = USERNAME_FIELD_INSETS;
        signInPanel.add(usernameLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = TEXT_FIELD_INSETS;
        signInPanel.add(usernameField, gridBagConstraints);
    }

    /**
     * Initializes the password field and adds it to the sign in panel.
     */
    private void initializeSignInPasswordField() {
        JLabel passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        passwordLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        passwordLabel.setForeground(Color.WHITE);
        passwordField.putClientProperty("JTextField.placeholderText", "Password");
        passwordField.setEchoChar('•');
        passwordField.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        passwordField.setForeground(Color.WHITE);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = PASSWORD_FIELD_INSETS;
        signInPanel.add(passwordLabel, gridBagConstraints);
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = TEXT_FIELD_INSETS;
        signInPanel.add(passwordField, gridBagConstraints);
    }

    /**
     * Initializes the "Remember me" checkbox and adds it to the sign in panel.
     */
    private void initializeRememberMeCheckBox() {
        JCheckBox rememberMeCheckBox = new JCheckBox("Remember me");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        rememberMeCheckBox.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        rememberMeCheckBox.setForeground(Color.WHITE);
        rememberMeCheckBox.setIconTextGap(10);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new Insets(0, 0, 25, 172);
        signInPanel.add(rememberMeCheckBox, gridBagConstraints);
    }

    /**
     * Initializes the "Forgot password?" label and adds it to the sign in panel.
     */
    private void initializeForgotPasswordLabel() {
        JLabel forgotPasswordLabel = new JLabel("Forgot password?");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        forgotPasswordLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        forgotPasswordLabel.setForeground(ACCENT_COLOUR);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new Insets(0, 175, 25, 0);
        signInPanel.add(forgotPasswordLabel, gridBagConstraints);
    }

    /**
     * Initializes the sign in button and adds it to the sign in panel.
     */
    private void initializeSignInButton() {
        DefaultButton signInButton = new DefaultButton("Sign In");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        signInButton.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        signInButton.setPreferredSize(BUTTON_DIMENSIONS);
        signInButton.addActionListener(event -> signIn());
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = BUTTON_INSETS;
        signInPanel.add(signInButton, gridBagConstraints);
    }

    /**
     * Initializes the sign up label and adds it to the sign in panel. When the sign up label is clicked, the sign up
     * panel is shown.
     */
    private void initializeSignUpLabel() {
        JLabel signUpQuestionLabel = new JLabel("Don't have an account?");
        JLabel signUpLabel = new JLabel("Sign Up!");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        signUpQuestionLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        signUpQuestionLabel.setForeground(Color.WHITE);
        signUpLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        signUpLabel.setForeground(ACCENT_COLOUR);
        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                remove(signInPanel);
                refresh();
                initializeSignUpPanel();
                refresh();
            }
        });
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new Insets(0, 0, 0, 70);
        signInPanel.add(signUpQuestionLabel, gridBagConstraints);
        gridBagConstraints.insets = new Insets(0, 185, 0, 0);
        signInPanel.add(signUpLabel, gridBagConstraints);
    }

    /**
     * Initializes the sign up panel and adds it to this entry panel.
     */
    private void initializeSignUpPanel() {
        signUpPanel = new JPanel(new GridBagLayout());
        JLabel signUpLabel = new JLabel("Sign Up");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        signUpLabel.setFont(HELVETICA_NEUE_LIGHT_HEADING_PLAIN);
        signUpLabel.setForeground(Color.WHITE);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 211, 50, 211);
        signUpPanel.add(signUpLabel, gridBagConstraints);
        initializeFirstNameField();
        initializeLastNameField();
        initializeSignUpUsernameField();
        initializeSignUpPasswordField();
        initializeSignUpButton();
        initializeSignInLabel();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = ZERO_INSETS;
        add(signUpPanel, gridBagConstraints);
    }

    /**
     * Initializes the first name field and adds it to the sign up panel.
     */
    private void initializeFirstNameField() {
        JLabel firstNameLabel = new JLabel("First Name");
        firstNameField = new JTextField(8);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        firstNameLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        firstNameLabel.setForeground(Color.WHITE);
        firstNameField.putClientProperty("JTextField.placeholderText", "First Name");
        firstNameField.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        firstNameField.setForeground(Color.WHITE);
        signUpFields.add(firstNameField);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, 0, 230);
        signUpPanel.add(firstNameLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, 25, 180);
        signUpPanel.add(firstNameField, gridBagConstraints);
    }

    /**
     * Initializes the last name field and adds it to the sign up panel.
     */
    private void initializeLastNameField() {
        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameField = new JTextField(8);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        lastNameLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        lastNameLabel.setForeground(Color.WHITE);
        lastNameField.putClientProperty("JTextField.placeholderText", "Last Name");
        lastNameField.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        lastNameField.setForeground(Color.WHITE);
        signUpFields.add(lastNameField);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 130, 0, 0);
        signUpPanel.add(lastNameLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 180, 25, 0);
        signUpPanel.add(lastNameField, gridBagConstraints);
    }

    /**
     * Initializes the username field and adds it to the sign up panel.
     */
    private void initializeSignUpUsernameField() {
        JLabel usernameLabel = new JLabel("Username");
        usernameField = new JTextField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        usernameLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        usernameLabel.setForeground(Color.WHITE);
        usernameField.putClientProperty("JTextField.placeholderText", "Username");
        usernameField.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        usernameField.setForeground(Color.WHITE);
        signUpFields.add(usernameField);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = USERNAME_FIELD_INSETS;
        signUpPanel.add(usernameLabel, gridBagConstraints);
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = TEXT_FIELD_INSETS;
        signUpPanel.add(usernameField, gridBagConstraints);
    }

    /**
     * Initializes the password field and adds it to the sign up panel.
     */
    private void initializeSignUpPasswordField() {
        JLabel passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField(20);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        passwordLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        passwordLabel.setForeground(Color.WHITE);
        passwordField.putClientProperty("JTextField.placeholderText", "Password");
        passwordField.setEchoChar('•');
        passwordField.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        passwordField.setForeground(Color.WHITE);
        signUpFields.add(passwordField);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = PASSWORD_FIELD_INSETS;
        signUpPanel.add(passwordLabel, gridBagConstraints);
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = TEXT_FIELD_INSETS;
        signUpPanel.add(passwordField, gridBagConstraints);
    }

    /**
     * Initializes the sign up button and adds it to the sign up panel.
     */
    private void initializeSignUpButton() {
        DefaultButton signUpButton = new DefaultButton("Sign Up");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        signUpButton.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        signUpButton.setPreferredSize(BUTTON_DIMENSIONS);
        signUpButton.addActionListener(event -> {
            try {
                signUp();
            } catch (EmptyNameException | NegativeAmountException | ZeroAmountException | DuplicateBudgetException
                    | DuplicateCategoryException exception) {
                exception.printStackTrace();
            }
        });
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = BUTTON_INSETS;
        signUpPanel.add(signUpButton, gridBagConstraints);
    }

    /**
     * Initializes the sign in label and adds it to the sign up panel. When the sign in label is clicked, the sign in
     * panel is shown.
     */
    private void initializeSignInLabel() {
        JLabel signInQuestionLabel = new JLabel("Already have an account?");
        JLabel signInLabel = new JLabel("Sign In!");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        signInQuestionLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        signInQuestionLabel.setForeground(Color.WHITE);
        signInLabel.setFont(HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        signInLabel.setForeground(ACCENT_COLOUR);
        signInLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                remove(signUpPanel);
                refresh();
                initializeSignInPanel();
                refresh();
            }
        });
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new Insets(0, 0, 0, 60);
        signUpPanel.add(signInQuestionLabel, gridBagConstraints);
        gridBagConstraints.insets = new Insets(0, 200, 0, 0);
        signUpPanel.add(signInLabel, gridBagConstraints);
    }

    /**
     * Initializes and sets the frame's menu bar.
     */
    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu accountMenu = new JMenu();
        JMenuItem saveMenuItem = new JMenuItem();
        JMenuItem signOutMenuItem = new JMenuItem();
        accountMenu.setText(account.getFirstName() + " " + account.getLastName());
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(event -> saveActionPerformed());
        signOutMenuItem.setText("Sign Out");
        signOutMenuItem.addActionListener(event -> signOutActionPerformed());
        accountMenu.add(saveMenuItem);
        accountMenu.add(signOutMenuItem);
        menuBar.add(accountMenu);
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setJMenuBar(menuBar);
    }

    /**
     * Initializes the sign out option pane and shows it.
     *
     * @return an integer from 0 to 2 in which 0 represents the "Yes" option, 1 represents the "No" option,
     * and 2 represents the "Cancel" option
     */
    private int initializeSignOutOptionPane() {
        String[] buttonLabels = new String[] {"Yes", "No", "Cancel"};
        String defaultOption = buttonLabels[0];
        return JOptionPane.showOptionDialog(this,
                "You have unsaved changes. Want to save your changes?", "bdgtr",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, buttonLabels, defaultOption);
    }

    /**
     * Signs in if the username and password are correct, try again otherwise.
     */
    private void signIn() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        try {
            account = jsonReader.read(username);
            if (password.equals(account.getPassword())) {
                signInSuccess();
            } else {
                signInFailure();
            }
        } catch (IOException | EmptyFirstNameException | EmptyLastNameException | EmptyUsernameException
                | EmptyPasswordException | EmptyNameException | NegativeAmountException | ZeroAmountException
                | DuplicateBudgetException | DuplicateCategoryException exception) {
            exception.printStackTrace();
        } catch (JSONException jsonException) {
            signInFailure();
        }
    }

    /**
     * Removes this entry panel and shows the main panel.
     *
     * @throws IOException if an error occurs reading data from file
     */
    private void signInSuccess() throws IOException {
        mainPanel = new MainPanel(account);
        initializeMenuBar();
        SwingUtilities.getWindowAncestor(this).add(mainPanel);
        refresh();
        SwingUtilities.getWindowAncestor(this).remove(this);
        refresh();
        playSound(SUCCESS_SOUND);
    }

    /**
     * Shows the "Incorrect username or password." error message dialog.
     */
    private void signInFailure() {
        usernameField.putClientProperty("JComponent.outline", "error");
        passwordField.putClientProperty("JComponent.outline", "error");
        refresh();
        playSound(ERROR_SOUND);
        JOptionPane.showMessageDialog(this, "Incorrect username or password.", "bdgtr",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Signs up if all fields are valid and username has not already been taken, try again otherwise.
     *
     * @throws EmptyNameException if the name has length zero
     * @throws NegativeAmountException if the amount is negative
     * @throws ZeroAmountException if the amount is zero
     * @throws DuplicateBudgetException if the budget already exists in this account
     * @throws DuplicateCategoryException if the category already exists in this budget
     */
    private void signUp() throws EmptyNameException, NegativeAmountException, ZeroAmountException,
            DuplicateBudgetException, DuplicateCategoryException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        try {
            jsonReader.read(username);
            signUpFailure("Username has already been taken.");
        } catch (IOException | EmptyFirstNameException | EmptyLastNameException | EmptyUsernameException
                | EmptyPasswordException exception) {
            exception.printStackTrace();
        } catch (JSONException jsonException) {
            try {
                account = new Account(firstName, lastName, username, password);
                signUpSuccess();
            } catch (EmptyFirstNameException | EmptyLastNameException | EmptyUsernameException
                    | EmptyPasswordException exception) {
                signUpFailure(exception.getMessage());
            }
        }
    }

    /**
     * Writes the account to file, shows the "Your account has been successfully created." message dialog, then shows
     * the home panel.
     */
    private void signUpSuccess() {
        try {
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();
            for (JTextField nextTextField : signUpFields) {
                nextTextField.putClientProperty("JComponent.outline", SUCCESS_COLOUR);
            }
            refresh();
            playSound(SUCCESS_SOUND);
            JOptionPane.showMessageDialog(this, "Your account has been successfully created!",
                    "bdgtr", JOptionPane.INFORMATION_MESSAGE);
            signInSuccess();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Shows the error message dialog with the error that caused the sign up to fail.
     *
     * @param errorMessage the error message
     */
    private void signUpFailure(String errorMessage) {
        for (JTextField nextTextField : signUpFields) {
            if (!nextTextField.getText().isEmpty()) {
                nextTextField.putClientProperty("JComponent.outline", SUCCESS_COLOURS);
            } else {
                nextTextField.putClientProperty("JComponent.outline", "error");
            }
        }
        if (errorMessage.equals("Username has already been taken.")) {
            usernameField.putClientProperty("JComponent.outline", "error");
        }
        refresh();
        playSound(ERROR_SOUND);
        JOptionPane.showMessageDialog(this, errorMessage, "bdgtr", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Signs out, shows this entry panel, and shows the "You have been signed out." message dialog.
     *
     * @throws IOException if an error occurs reading data from file
     */
    private void signOut() throws IOException {
        account = null;
        SwingUtilities.getWindowAncestor(mainPanel).add(new EntryPanel());
        SwingUtilities.getWindowAncestor(mainPanel).revalidate();
        SwingUtilities.getWindowAncestor(mainPanel).repaint();
        SwingUtilities.getWindowAncestor(mainPanel).remove(mainPanel);
        javax.swing.FocusManager.getCurrentManager().getActiveWindow().revalidate();
        javax.swing.FocusManager.getCurrentManager().getActiveWindow().repaint();
        refresh();
        JOptionPane.showMessageDialog(this, "You have been signed out.", "bdgtr",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Saves changes and shows the "Changes have been successfully saved." message dialog.
     */
    private void saveActionPerformed() {
        try {
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();
            JOptionPane.showMessageDialog(this, "Changes have been successfully saved.",
                    "bdgtr", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Signs out.
     */
    private void signOutActionPerformed() {
        try {
            handleSignOut();
        } catch (IOException | EmptyFirstNameException | EmptyLastNameException | EmptyUsernameException
                | EmptyPasswordException | EmptyNameException | NegativeAmountException | ZeroAmountException
                | DuplicateBudgetException | DuplicateCategoryException exception) {
            exception.printStackTrace();
        }
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
        if (account != null) {
            Account unsavedAccount = jsonReader.read(account.getUsername());
            if (!unsavedAccount.equals(account)) {
                hasUnsavedChanges = true;
            }
        }
        return hasUnsavedChanges;
    }

    /**
     * Provides the option to save changes to file if there are unsaved changes, sign out otherwise.
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
    private void handleSignOut() throws IOException, EmptyFirstNameException, EmptyLastNameException,
            EmptyUsernameException, EmptyPasswordException, EmptyNameException, NegativeAmountException,
            ZeroAmountException, DuplicateBudgetException, DuplicateCategoryException {
        if (hasUnsavedChanges()) {
            int option = initializeSignOutOptionPane();
            switch (option) {
                case JOptionPane.YES_OPTION:
                    jsonWriter.open();
                    jsonWriter.write(account);
                    jsonWriter.close();
                    signOut();
                    break;
                case JOptionPane.NO_OPTION:
                    signOut();
                case JOptionPane.CANCEL_OPTION:
                    break;
            }
        } else {
            signOut();
        }
    }

    /**
     * Plays the sound from the specified file.
     *
     * @param file the file with the sound to be played
     */
    public void playSound(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Gets the account that is signed in.
     *
     * @return the account that is signed in
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Refreshes this entry panel.
     */
    private void refresh() {
        revalidate();
        repaint();
    }
}
