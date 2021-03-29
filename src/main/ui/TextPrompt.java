package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;

/**
 * Represents a tool that displays a prompt over top of a text component when the Document of the text field is empty.
 * Code referenced from:
 * http://www.camick.com/java/source/TextPrompt.java
 */
public class TextPrompt extends JLabel implements FocusListener, DocumentListener {
    private JTextComponent component;
    private Document document;
    private Show show;

    public enum Show { ALWAYS, FOCUS_GAINED, FOCUS_LOST }

    /**
     * Creates a TextPrompt with the given text and the given component
     *
     * @param text the text to be shown
     * @param component the component on which the text will be shown
     */
    public TextPrompt(String text, JTextComponent component) {
        this(text, component, Show.ALWAYS);
    }

    /**
     * Creates a TextPrompt with the given text, the given component and the given Show enum.
     *
     * @param text the text to be shown
     * @param component the component on which the text will be shown
     * @param show the Show enum
     */
    public TextPrompt(String text, JTextComponent component, Show show) {
        this.component = component;
        setShow(show);
        document = component.getDocument();
        setText(text);
        setFont(component.getFont());
        setForeground(component.getForeground());
        setBorder(new EmptyBorder(component.getInsets()));
        setHorizontalAlignment(JLabel.LEADING);
        component.addFocusListener(this);
        document.addDocumentListener(this);
        component.setLayout(new BorderLayout());
        component.add(this);
        checkForPrompt();
    }

    /**
     * Sets the Show property to control when the prompt is shown.
     *
     * @param show a valid Show enum
     */
    public void setShow(Show show) {
        this.show = show;
    }

    /**
     * Checks whether the prompt should be visible.
     */
    private void checkForPrompt() {
        if (document.getLength() > 0) {
            setVisible(false);
            return;
        }
        if (component.hasFocus()) {
            setVisible(show == Show.ALWAYS || show == Show.FOCUS_GAINED);
        } else {
            setVisible(show == Show.ALWAYS || show == Show.FOCUS_LOST);
        }
    }

    /**
     * Checks for prompt when the focus is gained.
     *
     * @param event the Focus event
     */
    public void focusGained(FocusEvent event) {
        checkForPrompt();
    }

    /**
     * Checks for prompt when the focus is lost.
     *
     * @param event the Focus event
     */
    public void focusLost(FocusEvent event) {
        checkForPrompt();
    }

    /**
     * Inserts the update.
     *
     * @param event the Document event
     */
    public void insertUpdate(DocumentEvent event) {
        checkForPrompt();
    }

    /**
     * Removes the update.
     *
     * @param event the Document event
     */
    public void removeUpdate(DocumentEvent event) {
        checkForPrompt();
    }

    public void changedUpdate(DocumentEvent e) {}
}
