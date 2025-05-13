package controller;

import model.Customer;
import model.Parcel;
import collection.ParcelMap;
import collection.QueueOfCustomers;
import util.Log;

/**
 * Worker class - contains logic for a worker processing a customer
 */
public class Worker {
    private ParcelMap parcelMap;
    private QueueOfCustomers customerQueue;
    private Customer currentCustomer;
    private Parcel currentParcel;
    private double currentFee;
    private Log log;

    /**
     * Constructor for Worker
     * @param parcelMap ParcelMap to use
     * @param customerQueue QueueOfCustomers to use
     */
    public Worker(ParcelMap parcelMap, QueueOfCustomers customerQueue) {
        this.parcelMap = parcelMap;
        this.customerQueue = customerQueue;
        this.log = Log.getInstance();
        this.currentCustomer = null;
        this.currentParcel = null;
        this.currentFee = 0;
    }

    /**
     * Process the next customer in the queue
     * @return true if processed successfully, false if queue empty or parcel not found
     */
    public boolean processNextCustomer() {
        if (customerQueue.isEmpty()) {
            log.addLog("No customers in queue to process");
            return false;
        }

        // Get the next customer
        currentCustomer = customerQueue.removeCustomer(0);
        log.addLog("Processing customer: " + currentCustomer.getName());

        // Find the parcel
        currentParcel = parcelMap.findParcelByID(currentCustomer.getParcelID());
        if (currentParcel == null) {
            log.addLog("Parcel not found: " + currentCustomer.getParcelID());
            currentCustomer = null;
            return false;
        }

        // Calculate fee
        currentFee = calculateFee(currentParcel);
        log.addLog("Fee calculated: $" + String.format("%.2f", currentFee));

        // Mark parcel as collected
        parcelMap.markParcelAsCollected(currentParcel.getParcelID());
        log.addLog("Parcel " + currentParcel.getParcelID() + " collected by " + currentCustomer.getName());

        return true;
    }

    /**
     * Calculate fee for a parcel
     * @param parcel Parcel to calculate fee for
     * @return Fee amount
     */
    public double calculateFee(Parcel parcel) {
        // Base fee based on weight
        double fee = parcel.getWeight() * 0.5;
        
        // Add fee based on dimensions (volume)
        double volume = parcel.getLength() * parcel.getWidth() * parcel.getHeight();
        fee += volume * 0.01;
        
        // Add fee based on days in depot
        if (parcel.getDaysInDepot() <= 3) {
            // No additional fee for first 3 days
        } else if (parcel.getDaysInDepot() <= 7) {
            // Small fee for 4-7 days
            fee += 5.0;
        } else {
            // Larger fee for more than 7 days
            fee += 10.0;
        }
        
        // Special discount for parcels with ID starting with "X"
        if (parcel.getParcelID().startsWith("X")) {
            fee *= 0.9; // 10% discount
        }
        
        // Round to 2 decimal places
        return Math.round(fee * 100.0) / 100.0;
    }

    /**
     * Get the current customer
     * @return Current customer
     */
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * Get the current parcel
     * @return Current parcel
     */
    public Parcel getCurrentParcel() {
        return currentParcel;
    }

    /**
     * Get the current fee
     * @return Current fee
     */
    public double getCurrentFee() {
        return currentFee;
    }

    /**
     * Clear current transaction
     */
    public void clearCurrentTransaction() {
        currentCustomer = null;
        currentParcel = null;
        currentFee = 0;
    }
} 