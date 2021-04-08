package ui;

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

import javax.swing.*;
import java.io.IOException;

/**
 * Represents the launch class for the bdgtr application.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        FlatOneDarkIJTheme.install();
        UIManager.put("Button.arc", 12);
        UIManager.put("TextComponent.arc", 12);
        new Bdgtr();
    }
}
