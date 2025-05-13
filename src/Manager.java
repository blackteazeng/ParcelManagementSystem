import view.MainView;

/**
 * Manager class - this is the driver class for the application
 */
public class Manager {
    /**
     * Main method
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        try {
            // Set system look and feel
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
        
        // Create and show the main view
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainView mainView = new MainView();
                mainView.setVisible(true);
            }
        });
    }
} 