package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Represents a dialog that resets when it is closed.
 */
public class ResettableDialog extends JDialog {
    private final List<JTextField> textFields;

    /**
     * Creates a new dialog with the specified title and text fields that will reset when this dialog is closed.
     *
     * @param title the title of the dialog
     * @param textFields the text fields that will reset when this dialog is closed
     */
    public ResettableDialog(String title, List<JTextField> textFields) {
        super((Dialog) null, title);
        this.textFields = textFields;
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        for (JTextField nextTextField : textFields) {
            nextTextField.setText(null);
            nextTextField.putClientProperty("JComponent.outline", null);
        }
    }
}
