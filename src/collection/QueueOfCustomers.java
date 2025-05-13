package collection;

import model.Customer;
import util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * QueueOfCustomers class - to enable maintaining a queue/list of customer objects
 */
public class QueueOfCustomers {
    private List<Customer> customerQueue;
    private Log log;

    /**
     * Constructor for QueueOfCustomers
     */
    public QueueOfCustomers() {
        customerQueue = new ArrayList<>();
        log = Log.getInstance();
    }

    /**
     * Load customers from file
     * @param filename Name of file to load
     * @return true if successful, false otherwise
     */
    public boolean loadCustomersFromFile(String filename) {
        log.addLog("Starting to load customers from file: " + filename);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            
            String headerLine = reader.readLine();
            log.addLog("Header line: " + headerLine);
            
            if (headerLine == null) {
                log.addLog("Error: Empty customer file");
                return false;
            }
            
            int lineCount = 1; 
            
            while ((line = reader.readLine()) != null) {
                lineCount++;
                log.addLog("Processing line " + lineCount + ": " + line);
                
                if (line.trim().isEmpty()) {
                    log.addLog("Skipping empty line");
                    continue;
                }
                
                try {
                    String[] parts = line.split("\\s+");
                    
                    if (parts.length < 3) {
                        log.addLog("Line " + lineCount + " has insufficient data, need at least 3 parts but found " + parts.length);
                        continue;
                    }
                    
                    int seqNo = Integer.parseInt(parts[0]);
                    
                    String name;
                    String parcelID;
                    
                    if (parts.length == 3) {

                        name = parts[1];
                        parcelID = parts[2];
                    } else {
                        parcelID = parts[parts.length - 1];
                        
                        StringBuilder nameBuilder = new StringBuilder();
                        for (int i = 1; i < parts.length - 1; i++) {
                            nameBuilder.append(parts[i]).append(" ");
                        }
                        name = nameBuilder.toString().trim();
                    }
                    
                    log.addLog("Parsed customer: seqNo=" + seqNo + ", name=" + name + ", parcelID=" + parcelID);
                    
                    Customer customer = new Customer(seqNo, name, parcelID);
                    addCustomer(customer);
                } catch (NumberFormatException e) {
                    log.addLog("Error parsing sequence number in line " + lineCount + ": " + e.getMessage());
                } catch (Exception e) {
                    log.addLog("Unexpected error processing line " + lineCount + ": " + e.getMessage());
                }
            }
            
            int loadedCount = customerQueue.size();
            log.addLog("Loaded " + loadedCount + " customers from file");
            
            return loadedCount > 0;
        } catch (IOException e) {
            log.addLog("IO Error loading customers: " + e.getMessage());
            return false;
        }
    }

    /**
     * Add a customer to the queue
     * @param customer Customer to add
     */
    public void addCustomer(Customer customer) {
        customerQueue.add(customer);
        log.addLog("Customer added to queue: " + customer.getName() + " for parcel " + customer.getParcelID());
    }

    /**
     * Remove a customer from the queue
     * @param index Index of customer to remove
     * @return Removed customer or null if index invalid
     */
    public Customer removeCustomer(int index) {
        if (index >= 0 && index < customerQueue.size()) {
            Customer customer = customerQueue.remove(index);
            log.addLog("Customer removed from queue: " + customer.getName());
            
            // Update sequence numbers for remaining customers
            for (int i = 0; i < customerQueue.size(); i++) {
                customerQueue.get(i).setSeqNo(i + 1);
            }
            
            return customer;
        }
        return null;
    }

    /**
     * Get the first customer in the queue
     * @return First customer or null if queue empty
     */
    public Customer getFirstCustomer() {
        if (!customerQueue.isEmpty()) {
            return customerQueue.get(0);
        }
        return null;
    }

    /**
     * Get the queue size
     * @return Size of queue
     */
    public int size() {
        return customerQueue.size();
    }

    /**
     * Check if queue is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return customerQueue.isEmpty();
    }

    /**
     * Get all customers
     * @return List of all customers
     */
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerQueue);
    }
} 