package ui;

import javax.swing.table.DefaultTableModel;

/**
 * Represents a table model that is not editable.
 */
public class NotEditableTableModel extends DefaultTableModel {

    /**
     * Constructs a new table model with the specified column names and the number of rows.
     *  @param columnNames the names of the columns
     * @param rowCount the number of rows the table holds
     */
    public NotEditableTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
