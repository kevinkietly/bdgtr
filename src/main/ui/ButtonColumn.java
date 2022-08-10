package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Represents an editor and renderer that looks like a button.
 * Code referenced from:
 * https://tips4java.wordpress.com/2009/07/12/table-button-column/
 */
public class ButtonColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer, ActionListener,
        MouseListener {
    private Object editorValue;
    private boolean isButtonColumnEditor;
    private JTable table;
    private JButton editButton;
    private JButton renderButton;
    private Action action;

    /**
     * Creates a new button column to be used as an editor and renderer.
     * The editor and renderer will automatically be installed on the table column of the specified column.
     *
     * @param table the table containing the button editor and renderer
     * @param action the action to be invoked when the button is clicked
     * @param column the column to which the button editor and renderer is added
     */
    public ButtonColumn(JTable table, Action action, int column) {
        this.table = table;
        this.action = action;
        renderButton = new JButton();
        editButton = new JButton();
        TableColumnModel columnModel = table.getColumnModel();
        renderButton.setPreferredSize(new Dimension(30, 30));
        renderButton.setBorderPainted(false);
        editButton.setFocusPainted(false);
        editButton.addActionListener(this);
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
        table.addMouseListener(this);
    }

    /**
     * Enables or disables the button.
     *
     * @param isEnabled true to enable the button, false otherwise
     */
    public void setEnabled(boolean isEnabled) {
        renderButton.setEnabled(isEnabled);
        editButton.setEnabled(isEnabled);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value == null) {
            editButton.setText("");
            editButton.setIcon(null);
        } else if (value instanceof Icon) {
            editButton.setText("");
            editButton.setIcon((Icon) value);
        } else {
            editButton.setText(value.toString());
            editButton.setIcon(null);
        }
        this.editorValue = value;
        return editButton;
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        renderButton.setBorder(editButton.getBorder());
        if (isSelected) {
            renderButton.setBackground(table.getSelectionBackground());
            renderButton.setForeground(table.getSelectionForeground());
        } else {
            renderButton.setBackground(UIManager.getColor("Button.background"));
            renderButton.setForeground(table.getForeground());
        }
        if (value instanceof Icon) {
            renderButton.setText("");
            renderButton.setIcon((Icon) value);
        }
        return renderButton;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        int row = table.convertRowIndexToModel(table.getEditingRow());
        fireEditingStopped();
        ActionEvent actionEvent = new ActionEvent(table, ActionEvent.ACTION_PERFORMED, "" + row);
        action.actionPerformed(actionEvent);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (table.isEditing() && table.getCellEditor() == this) {
            isButtonColumnEditor = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (isButtonColumnEditor && table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        isButtonColumnEditor = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
