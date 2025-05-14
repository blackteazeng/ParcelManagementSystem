package view;

import model.Customer;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerTableModel class - Model for customer table
 */
public class CustomerTableModel extends AbstractTableModel {
    private List<Customer> customers;
    private final String[] columnNames = {"Seq No", "Name", "Parcel ID"};

    /**
     * Constructor for CustomerTableModel
     */
    public CustomerTableModel() {
        this.customers = new ArrayList<>();
    }

    /**
     * Set customers to display
     * @param customers List of customers to display
     */
    public void setCustomers(List<Customer> customers) {
        this.customers = new ArrayList<>(customers);
        fireTableDataChanged();
    }
    
    /**
     * Get the customer at the specified row
     * @param row Row index
     * @return Customer at row
     */
    public Customer getCustomerAt(int row) {
        if (row >= 0 && row < customers.size()) {
            return customers.get(row);
        }
        return null;
    }
    
    @Override
    public int getRowCount() {
        return customers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < customers.size()) {
            Customer customer = customers.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return customer.getSeqNo();
                case 1:
                    return customer.getName();
                case 2:
                    return customer.getParcelID();
                default:
                    return "N/A";
            }
        }
        return "N/A";
    }
} 