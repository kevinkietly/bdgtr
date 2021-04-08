package ui;

import javax.swing.*;

/**
 * Represents a button that makes itself the default button of the frame to which it has been added.
 * Code referenced from:
 * https://stackoverflow.com/questions/6287145/jbutton-subclass-that-sets-itself-to-be-the-default-button-for-a-jframe
 */
public class DefaultButton extends JButton {

    /**
     * Creates a new button with the specified text.
     *
     * @param text the text of the button
     */
    public DefaultButton(String text) {
        super(text);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.getRootPane(this).setDefaultButton(this);
    }
}
