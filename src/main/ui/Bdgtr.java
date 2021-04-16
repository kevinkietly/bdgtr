package ui;

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Represents the launch class for the bdgtr application.
 */
public class Bdgtr implements FontRepository, ColourRepository {

    public static void main(String[] args) throws IOException {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        FlatOneDarkIJTheme.install();
        UIManager.put("Button.arc", 12);
        UIManager.put("Component.arc", 12);
        UIManager.put("ProgressBar.arc", 12);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("Component.arrowType", "chevron");
        UIManager.put("Component.focusWidth", 3);
        UIManager.put("CheckBox.font", HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        UIManager.put("CheckBox.foreground", Color.WHITE);
        UIManager.put("OptionPane.buttonFont", HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        UIManager.put("OptionPane.messageFont", HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("PasswordField.echoChar", '•');
        UIManager.put("PasswordField.font", HELVETICA_NEUE_LIGHT_SUBHEADING_PLAIN);
        UIManager.put("PasswordField.foreground", Color.WHITE);
        UIManager.put("ProgressBar.font", HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        UIManager.put("Table.font", HELVETICA_NEUE_LIGHT_BODY_PLAIN);
        UIManager.put("Table.foreground", Color.WHITE);
        UIManager.put("TextField.foreground", Color.WHITE);
        new MainWindow();
    }
}
