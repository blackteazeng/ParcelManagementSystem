package view;

import model.Parcel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * ParcelTableModel class - Model for parcel table
 */
public class ParcelTableModel extends AbstractTableModel {
    private List<Parcel> parcels;
    private final String[] columnNames = {"Parcel ID", "Days in Depot", "Weight", "Dimensions", "Status"};

    /**
     * Constructor for ParcelTableModel
     */
    public ParcelTableModel() {
        this.parcels = new ArrayList<>();
    }

    /**
     * Set parcels to display
     * @param parcels List of parcels to display
     */
    public void setParcels(List<Parcel> parcels) {
        this.parcels = new ArrayList<>(parcels);
        fireTableDataChanged();
    }
    
    /**
     * Get the parcel at the specified row
     * @param row Row index
     * @return Parcel at row
     */
    public Parcel getParcelAt(int row) {
        if (row >= 0 && row < parcels.size()) {
            return parcels.get(row);
        }
        return null;
    }
    
    @Override
    public int getRowCount() {
        return parcels.size();
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
        if (rowIndex >= 0 && rowIndex < parcels.size()) {
            Parcel parcel = parcels.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return parcel.getParcelID();
                case 1:
                    return parcel.getDaysInDepot();
                case 2:
                    return parcel.getWeight();
                case 3:
                    return parcel.getDimensions();
                case 4:
                    return parcel.isCollected() ? "Collected" : "Waiting";
                default:
                    return "N/A";
            }
        }
        return "N/A";
    }
} 