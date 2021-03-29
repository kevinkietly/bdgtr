package ui;

import model.Account;
import model.exceptions.*;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Represents the Landing panel.
 */
public class LandingPanel extends JPanel {
    private static final Color ACCENT_COLOUR = new Color(86, 138, 242);
    private static final String JSON_STORE = "./data/accounts.json";

    private Account account;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JButton signInButton;
    private JButton signUpButton;
    private boolean isSignedIn;


    /**
     * Creates the Landing panel.
     *
     * @throws IOException if an error occurs reading data from the file
     */
    public LandingPanel() throws IOException {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        initializeJson();
        initializeComponents();
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
        initializeOverlayPanel();
        initializeSignInPanel();
    }

    /**
     * Initializes the overlay panel.
     *
     * @throws IOException if an error occurs reading data from the file
     */
    private void initializeOverlayPanel() throws IOException {
        JPanel overlayPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        BufferedImage overlayImage = ImageIO.read(new File("./images/BDGTR.png"));
        ImageIcon overlayIcon = new ImageIcon(overlayImage);
        JLabel overlayLabel = new JLabel(overlayIcon);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        overlayPanel.add(overlayLabel, gridBagConstraints);
        add(overlayPanel);
    }

    /**
     * Initializes the sign in panel.
     */
    private void initializeSignInPanel() {
        JPanel signInPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JLabel signInLabel = new JLabel("Sign In");
        signInLabel.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 36));
        signInLabel.setForeground(Color.WHITE);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(0, 108, 36, 108);
        signInPanel.add(signInLabel, gridBagConstraints);
        initializeUsernameTextField(signInPanel);
        initializePasswordTextField(signInPanel);
        initializeRememberMeButton(signInPanel);
        initializeForgotPasswordLabel(signInPanel);
        initializeSignInButton(signInPanel);
        initializeSignUpLabel(signInPanel);
        add(signInPanel);
    }

    /**
     * Initializes the username text field.
     *
     * @param signInPanel the sign in panel
     */
    private void initializeUsernameTextField(JPanel signInPanel) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JLabel usernameLabel = new JLabel("Username");
        usernameTextField = new JTextField(20);
        TextPrompt usernameTextPrompt = new TextPrompt("Enter your username", usernameTextField);
        usernameLabel.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 18));
        usernameLabel.setForeground(Color.WHITE);
        usernameTextField.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 18));
        usernameTextPrompt.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 18));
        usernameTextPrompt.setForeground(Color.GRAY);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 36, 12, 0);
        signInPanel.add(usernameLabel, gridBagConstraints);
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 36, 12, 36);
        signInPanel.add(usernameTextField, gridBagConstraints);
    }

    /**
     * Initializes the password text field.
     *
     * @param signInPanel the sign in panel
     */
    private void initializePasswordTextField(JPanel signInPanel) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JLabel passwordLabel = new JLabel("Password");
        passwordTextField = new JPasswordField(20);
        TextPrompt usernameTextPrompt = new TextPrompt("Enter your password", passwordTextField);
        passwordLabel.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 18));
        passwordLabel.setForeground(Color.WHITE);
        passwordTextField.setEchoChar('•');
        passwordTextField.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 18));
        usernameTextPrompt.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 18));
        usernameTextPrompt.setForeground(Color.GRAY);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(0, 36, 12, 0);
        signInPanel.add(passwordLabel, gridBagConstraints);
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new Insets(0, 36, 12, 36);
        signInPanel.add(passwordTextField, gridBagConstraints);
    }

    /**
     * Initializes the "Remember Me" button.
     *
     * @param signInPanel the sign in panel
     */
    private void initializeRememberMeButton(JPanel signInPanel) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JCheckBox rememberMeButton = new JCheckBox("Remember Me");
        rememberMeButton.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 15));
        rememberMeButton.setForeground(Color.WHITE);
        rememberMeButton.setIconTextGap(9);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new Insets(0, -306, 12, 0);
        signInPanel.add(rememberMeButton, gridBagConstraints);
    }

    /**
     * Initializes the "Forgot password?" label.
     *
     * @param signInPanel the sign in panel
     */
    private void initializeForgotPasswordLabel(JPanel signInPanel) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JLabel forgotPasswordLabel = new JLabel("Forgot password?");
        forgotPasswordLabel.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 15));
        forgotPasswordLabel.setForeground(ACCENT_COLOUR);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new Insets(0, 0, 9, -81);
        signInPanel.add(forgotPasswordLabel, gridBagConstraints);
    }

    /**
     * Initializes the sign in button.
     *
     * @param signInPanel the sign in panel
     */
    private void initializeSignInButton(JPanel signInPanel) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        signInButton = new JButton("Sign In");
        signInButton.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 18));
        signInButton.setForeground(Color.WHITE);
        signInButton.setPreferredSize(new Dimension(315, 36));
        signInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                signIn();
            }
        });
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new Insets(0, -114, 12, 0);
        signInPanel.add(signInButton, gridBagConstraints);
    }

    /**
     * Initializes the sign up label.
     *
     * @param signInPanel the sign in panel
     */
    private void initializeSignUpLabel(JPanel signInPanel) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JLabel signUpQuestionLabel = new JLabel("Don't have an account?");
        JLabel signUpLabel = new JLabel("Sign Up!");
        signUpQuestionLabel.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 15));
        signUpQuestionLabel.setForeground(Color.WHITE);
        signUpLabel.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 15));
        signUpLabel.setForeground(ACCENT_COLOUR);
        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                removeAndRefresh(signInPanel);
                initializeSignUpPanel();
            }
        });
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new Insets(0, -171, 12, 0);
        signInPanel.add(signUpQuestionLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new Insets(0, 0, 12, -45);
        signInPanel.add(signUpLabel, gridBagConstraints);
    }

    /**
     * Initializes the sign up panel.
     */
    private void initializeSignUpPanel() {
        JPanel signUpPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JLabel signInLabel = new JLabel("Sign Up");
        signInLabel.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 36));
        signInLabel.setForeground(Color.WHITE);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(0, 108, 36, 108);
        signUpPanel.add(signInLabel, gridBagConstraints);
        initializeUsernameTextField(signUpPanel);
        initializePasswordTextField(signUpPanel);
        initializeSignUpButton(signUpPanel);
        initializeSignInLabel(signUpPanel);
        add(signUpPanel);
    }

    /**
     * Initializes the sign up button.
     *
     * @param signUpPanel the sign up panel
     */
    private void initializeSignUpButton(JPanel signUpPanel) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 18));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setPreferredSize(new Dimension(315, 36));
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new Insets(0, -114, 12, 0);
        signUpPanel.add(signUpButton, gridBagConstraints);
    }

    /**
     * Initializes the sign in label.
     *
     * @param signUpPanel the sign up panel
     */
    private void initializeSignInLabel(JPanel signUpPanel) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JLabel signInQuestionLabel = new JLabel("Already have an account?");
        JLabel signInLabel = new JLabel("Sign In!");
        signInQuestionLabel.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 15));
        signInQuestionLabel.setForeground(Color.WHITE);
        signInLabel.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 15));
        signInLabel.setForeground(ACCENT_COLOUR);
        signInLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                removeAndRefresh(signUpPanel);
                initializeSignInPanel();
            }
        });
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new Insets(0, -168, 12, 0);
        signUpPanel.add(signInQuestionLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new Insets(0, 0, 12, -54);
        signUpPanel.add(signInLabel, gridBagConstraints);
    }

    /**
     * Signs in user.
     */
    private void signIn() {
        String username = usernameTextField.getText();
        String password = new String(passwordTextField.getPassword());
        try {
            account = jsonReader.read(username);
            if (username.equals(account.getUsername()) && password.equals(account.getPassword())) {
                removeAndRefresh(this);
                DashboardPanel dashboardPanel = new DashboardPanel();
            }
        } catch (IOException | EmptyUsernameException | EmptyPasswordException | EmptyNameException
                | NegativeAmountException | DuplicateBudgetException | DuplicateCategoryException
                | NegativeCostException exception) {
            exception.printStackTrace();
        } catch (JSONException jsonException) {
            isSignedIn = false;
        }
    }

    /**
     * Removes the given component and refreshes.
     *
     * @param component the component to be removed
     */
    private void removeAndRefresh(Component component) {
        remove(component);
        revalidate();
        repaint();
    }
}
