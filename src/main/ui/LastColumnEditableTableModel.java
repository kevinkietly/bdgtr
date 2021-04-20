package ui;

import javax.swing.table.DefaultTableModel;

/**
 * Represents a table model whose last column is editable.
 */
public class LastColumnEditableTableModel extends DefaultTableModel {

    /**
     * Creates a new table model with the specified column names and the number of rows.
     *
     * @param columnNames the names of the columns
     * @param rowCount the number of rows the table holds
     */
    public LastColumnEditableTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == this.getColumnCount() - 1;
    }
}
