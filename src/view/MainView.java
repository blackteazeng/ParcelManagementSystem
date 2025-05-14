package view;

import controller.Worker;
import model.Customer;
import model.Parcel;
import collection.ParcelMap;
import collection.QueueOfCustomers;
import util.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * MainView class - Main GUI window of the application
 */
public class MainView extends JFrame {
    // Models
    private ParcelMap parcelMap;
    private QueueOfCustomers queueOfCustomers;
    private Worker worker;
    private Log log;
    
    // Table models
    private ParcelTableModel parcelTableModel;
    private CustomerTableModel customerTableModel;
    
    // UI Components
    private JTable parcelTable;
    private JTable customerTable;
    private JPanel currentParcelPanel;
    private JLabel lblCurrentCustomer;
    private JLabel lblCurrentParcel;
    private JLabel lblCurrentFee;
    
    // File paths
    private String parcelFilePath = "parcels.txt";
    private String customerFilePath = "customers.txt";
    private String logFilePath = "log.txt";
    
    /**
     * Constructor for MainView
     */
    public MainView() {
        // Initialize models
        parcelMap = new ParcelMap();
        queueOfCustomers = new QueueOfCustomers();
        worker = new Worker(parcelMap, queueOfCustomers);
        log = Log.getInstance();
        
        // Initialize UI
        initializeUI();
        
        // Load data
        loadData();
    }
    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        setTitle("Depot Parcel System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create table models
        parcelTableModel = new ParcelTableModel();
        customerTableModel = new CustomerTableModel();
        
        // Create tables
        parcelTable = new JTable(parcelTableModel);
        customerTable = new JTable(customerTableModel);
        
        // Create scroll panes for tables
        JScrollPane parcelScrollPane = new JScrollPane(parcelTable);
        JScrollPane customerScrollPane = new JScrollPane(customerTable);
        
        // Create titled borders for tables
        parcelScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
                "Parcels", TitledBorder.LEFT, TitledBorder.TOP));
        customerScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
                "Customers Queue", TitledBorder.LEFT, TitledBorder.TOP));
        
        // Create panels for tables
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        tablesPanel.add(parcelScrollPane);
        tablesPanel.add(customerScrollPane);
        
        // Create current parcel panel
        createCurrentParcelPanel();
        
        // Create action panel
        JPanel actionPanel = createActionPanel();
        
        // Add components to main panel
        mainPanel.add(tablesPanel, BorderLayout.CENTER);
        mainPanel.add(currentParcelPanel, BorderLayout.NORTH);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        
        // Set content pane
        setContentPane(mainPanel);
    }
    
    /**
     * Create panel for current parcel being processed
     */
    private void createCurrentParcelPanel() {
        currentParcelPanel = new JPanel(new GridLayout(3, 1));
        currentParcelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
                "Current Processing", TitledBorder.LEFT, TitledBorder.TOP));
        
        lblCurrentCustomer = new JLabel("Customer: None");
        lblCurrentParcel = new JLabel("Parcel: None");
        lblCurrentFee = new JLabel("Fee: £0.00");
        
        Font labelFont = new Font(lblCurrentCustomer.getFont().getName(), Font.BOLD, 14);
        lblCurrentCustomer.setFont(labelFont);
        lblCurrentParcel.setFont(labelFont);
        lblCurrentFee.setFont(labelFont);
        
        currentParcelPanel.add(lblCurrentCustomer);
        currentParcelPanel.add(lblCurrentParcel);
        currentParcelPanel.add(lblCurrentFee);
    }
    
    /**
     * Create panel for action buttons
     * @return JPanel containing action buttons
     */
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnProcessNext = new JButton("Process Next Customer");
        JButton btnAddCustomer = new JButton("Add New Customer");
        JButton btnAddParcel = new JButton("Add New Parcel");
        JButton btnFindParcel = new JButton("Find Parcel");
        JButton btnGenerateReport = new JButton("Generate Report");
        
        // Add action listeners
        btnProcessNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processNextCustomer();
            }
        });
        
        btnAddCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddCustomerDialog();
            }
        });
        
        btnAddParcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddParcelDialog();
            }
        });
        
        btnFindParcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFindParcelDialog();
            }
        });
        
        btnGenerateReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        
        // Add buttons to panel
        actionPanel.add(btnProcessNext);
        actionPanel.add(btnAddCustomer);
        actionPanel.add(btnAddParcel);
        actionPanel.add(btnFindParcel);
        actionPanel.add(btnGenerateReport);
        
        return actionPanel;
    }
    
    /**
     * Load data from files
     */
    private void loadData() {
        log.addLog("Attempting to load data files...");
        
        File parcelFile = new File(parcelFilePath);
        File customerFile = new File(customerFilePath);
        
        if (!parcelFile.exists()) {
            log.addLog("Parcel file not found at: " + parcelFile.getAbsolutePath());

            parcelFile = new File("../" + parcelFilePath);
            parcelFilePath = "../" + parcelFilePath;
            if (parcelFile.exists()) {
                log.addLog("Found parcel file in parent directory: " + parcelFile.getAbsolutePath());
            } else {

                log.addLog("Current working directory: " + System.getProperty("user.dir"));
                JOptionPane.showMessageDialog(this,
                        "Parcel file not found: " + parcelFilePath,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        if (!customerFile.exists()) {
            log.addLog("Customer file not found at: " + customerFile.getAbsolutePath());
  
            customerFile = new File("../" + customerFilePath);
            customerFilePath = "../" + customerFilePath;
            if (customerFile.exists()) {
                log.addLog("Found customer file in parent directory: " + customerFile.getAbsolutePath());
            } else {
                log.addLog("Current working directory: " + System.getProperty("user.dir"));
                JOptionPane.showMessageDialog(this,
                        "Customer file not found: " + customerFilePath,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        

        log.addLog("Loading parcels from: " + parcelFilePath);
        if (parcelMap.loadParcelsFromFile(parcelFilePath)) {
            updateParcelTable();
            log.addLog("Successfully loaded parcels: " + parcelMap.getAllParcels().size());
        } else {
            log.addLog("Failed to load parcels from: " + parcelFilePath);
            JOptionPane.showMessageDialog(this, 
                    "Error loading parcels from file: " + parcelFilePath, 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        

        log.addLog("Loading customers from: " + customerFilePath);
        if (queueOfCustomers.loadCustomersFromFile(customerFilePath)) {
            updateCustomerTable();
            log.addLog("Successfully loaded customers: " + queueOfCustomers.size());
        } else {
            log.addLog("Failed to load customers from: " + customerFilePath);
            JOptionPane.showMessageDialog(this, 
                    "Error loading customers from file: " + customerFilePath, 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Update the parcel table
     */
    private void updateParcelTable() {
        parcelTableModel.setParcels(parcelMap.getAllParcels());
    }
    
    /**
     * Update the customer table
     */
    private void updateCustomerTable() {
        customerTableModel.setCustomers(queueOfCustomers.getAllCustomers());
    }
    
    /**
     * Update current processing panel
     */
    private void updateCurrentProcessingPanel() {
        Customer customer = worker.getCurrentCustomer();
        Parcel parcel = worker.getCurrentParcel();
        double fee = worker.getCurrentFee();
        
        if (customer != null) {
            lblCurrentCustomer.setText("Customer: " + customer.getName());
            lblCurrentParcel.setText("Parcel: " + parcel.getParcelID());
            lblCurrentFee.setText("Fee: £" + String.format("%.2f", fee));
        } else {
            lblCurrentCustomer.setText("Customer: None");
            lblCurrentParcel.setText("Parcel: None");
            lblCurrentFee.setText("Fee: £0.00");
        }
    }
    
    /**
     * Process the next customer
     */
    private void processNextCustomer() {
        if (worker.processNextCustomer()) {
            updateParcelTable();
            updateCustomerTable();
            updateCurrentProcessingPanel();
        } else {
            JOptionPane.showMessageDialog(this, 
                    "No customers to process or parcel not found", 
                    "Processing Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Show dialog to add a new customer
     */
    private void showAddCustomerDialog() {
        JTextField nameField = new JTextField(20);
        JTextField parcelIDField = new JTextField(10);
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Customer Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Parcel ID:"));
        panel.add(parcelIDField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Customer", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String parcelID = parcelIDField.getText().trim();
            
            if (!name.isEmpty() && !parcelID.isEmpty()) {
                // Create customer with next sequence number
                int seqNo = queueOfCustomers.size() + 1;
                Customer newCustomer = new Customer(seqNo, name, parcelID);
                queueOfCustomers.addCustomer(newCustomer);
                
                // Update UI
                updateCustomerTable();
                JOptionPane.showMessageDialog(this, 
                        "Customer added successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Name and Parcel ID cannot be empty", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Show dialog to add a new parcel
     */
    private void showAddParcelDialog() {
        JTextField parcelIDField = new JTextField(10);
        JTextField daysField = new JTextField(5);
        JTextField weightField = new JTextField(10);
        JTextField lengthField = new JTextField(5);
        JTextField widthField = new JTextField(5);
        JTextField heightField = new JTextField(5);
        
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Parcel ID:"));
        panel.add(parcelIDField);
        panel.add(new JLabel("Days in Depot:"));
        panel.add(daysField);
        panel.add(new JLabel("Weight:"));
        panel.add(weightField);
        panel.add(new JLabel("Length:"));
        panel.add(lengthField);
        panel.add(new JLabel("Width:"));
        panel.add(widthField);
        panel.add(new JLabel("Height:"));
        panel.add(heightField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Parcel", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String parcelID = parcelIDField.getText().trim();
                int days = Integer.parseInt(daysField.getText().trim());
                double weight = Double.parseDouble(weightField.getText().trim());
                int length = Integer.parseInt(lengthField.getText().trim());
                int width = Integer.parseInt(widthField.getText().trim());
                int height = Integer.parseInt(heightField.getText().trim());
                
                if (!parcelID.isEmpty()) {
                    // Create and add parcel
                    Parcel newParcel = new Parcel(parcelID, days, weight, length, width, height);
                    parcelMap.addParcel(newParcel);
                    
                    // Update UI
                    updateParcelTable();
                    JOptionPane.showMessageDialog(this, 
                            "Parcel added successfully", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "Parcel ID cannot be empty", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                        "Invalid input. Please enter valid numbers.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Show dialog to find a parcel
     */
    private void showFindParcelDialog() {
        String parcelID = JOptionPane.showInputDialog(this, 
                "Enter Parcel ID to find:", 
                "Find Parcel", JOptionPane.QUESTION_MESSAGE);
        
        if (parcelID != null && !parcelID.isEmpty()) {
            Parcel parcel = parcelMap.findParcelByID(parcelID);
            
            if (parcel != null) {
                // Show parcel details
                JOptionPane.showMessageDialog(this, 
                        "Parcel Found:\n" + 
                        "ID: " + parcel.getParcelID() + "\n" +
                        "Days in Depot: " + parcel.getDaysInDepot() + "\n" +
                        "Weight: " + parcel.getWeight() + "\n" +
                        "Dimensions: " + parcel.getDimensions() + "\n" +
                        "Status: " + (parcel.isCollected() ? "Collected" : "Waiting"),
                        "Parcel Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "No parcel found with ID: " + parcelID, 
                        "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    /**
     * Generate report and save to file
     */
    private void generateReport() {
        try {
            // Output report to log
            log.addLog("=== DEPOT PARCEL SYSTEM REPORT ===");
            log.addLog("Total parcels: " + parcelMap.getAllParcels().size());
            log.addLog("Collected parcels: " + parcelMap.getCollectedParcels().size());
            log.addLog("Waiting parcels: " + parcelMap.getUncollectedParcels().size());
            
            log.addLog("\nCOLLECTED PARCELS:");
            for (Parcel parcel : parcelMap.getCollectedParcels()) {
                log.addLog(parcel.getParcelID() + " - Days: " + parcel.getDaysInDepot() + 
                        " - Weight: " + parcel.getWeight() + " - Dimensions: " + parcel.getDimensions() +
                        " - Fee: £" + String.format("%.2f", worker.calculateFee(parcel)));
            }
            
            log.addLog("\nWAITING PARCELS:");
            for (Parcel parcel : parcelMap.getUncollectedParcels()) {
                log.addLog(parcel.getParcelID() + " - Days: " + parcel.getDaysInDepot() + 
                        " - Weight: " + parcel.getWeight() + " - Dimensions: " + parcel.getDimensions());
            }
            
            log.addLog("\nSTATISTICS:");
            log.addLog("Parcels in depot more than 7 days: " + parcelMap.countParcelsInDepotMoreThanDays(7));
            
            // Calculate total fees collected
            double totalFees = 0;
            for (Parcel parcel : parcelMap.getCollectedParcels()) {
                totalFees += worker.calculateFee(parcel);
            }
            log.addLog("Total fees collected: £" + String.format("%.2f", totalFees));
            
            log.addLog("=== END OF REPORT ===");
            
            // Save to file
            if (log.saveToFile(logFilePath)) {
                JOptionPane.showMessageDialog(this, 
                        "Report generated successfully and saved to " + logFilePath, 
                        "Report Generated", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error saving report to file", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error generating report: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
} 