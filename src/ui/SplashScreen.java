package ui;

// Import necessary classes for creating a graphical user interface
import javax.swing.*; // For Swing components like JWindow, JPanel, JLabel, and JProgressBar
import java.awt.*;    // For layout management and graphics like GradientPaint

// Define the SplashScreen class that extends JWindow
// JWindow provides an undecorated top-level window suitable for splash screens
public class SplashScreen extends JWindow {
    private JProgressBar progressBar; // Declare a progress bar to display loading progress

    // Constructor for initializing the splash screen
    public SplashScreen() {
        // Set up splash screen window
        setSize(600, 300); // Define the dimensions of the splash screen (600x300 pixels)
        setLocationRelativeTo(null); // Automatically center the splash screen on the screen

        // Create a main panel with a custom paintComponent method to add a gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // Call the superclass method to ensure proper rendering
                // Add a gradient background using Graphics2D and GradientPaint
                Graphics2D g2d = (Graphics2D) g; // Cast Graphics to Graphics2D for advanced rendering
                Color color1 = new Color(50, 150, 250); // Define the first color (blue)
                Color color2 = new Color(100, 200, 250); // Define the second color (light blue)
                // Create a gradient from top to bottom
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient); // Apply the gradient paint
                g2d.fillRect(0, 0, getWidth(), getHeight()); // Fill the panel with the gradient
            }
        };

        // Set the layout for the panel to BorderLayout for easy placement of components
        mainPanel.setLayout(new BorderLayout());

        // Create and configure a title label to display a welcome message
        JLabel titleLabel = new JLabel("Welcome to the Package Tracking System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28)); // Set font style and size
        titleLabel.setForeground(Color.WHITE); // Set text color to white for visibility against the gradient
        mainPanel.add(titleLabel, BorderLayout.CENTER); // Position the label in the center of the panel

        // Initialize the progress bar for the splash screen
        progressBar = new JProgressBar(); // Create a progress bar instance
        progressBar.setMinimum(0); // Set the minimum value to 0
        progressBar.setMaximum(100); // Set the maximum value to 100
        progressBar.setStringPainted(true); // Enable text display showing percentage progress
        progressBar.setBackground(Color.WHITE); // Set the background color of the progress bar
        progressBar.setForeground(new Color(50, 150, 250)); // Set the progress bar color to blue
        mainPanel.add(progressBar, BorderLayout.SOUTH); // Position the progress bar at the bottom

        // Add the main panel to the window
        add(mainPanel);
        setVisible(true); // Make the splash screen visible

        // Simulate loading progress
        loadProgress();
    }

    // Method to simulate loading progress and display the progress bar
    private void loadProgress() {
        try {
            // Iterate from 0 to 100 to simulate progress
            for (int i = 0; i <= 100; i++) {
                progressBar.setValue(i); // Set the current progress value
                Thread.sleep(30); // Pause execution to create a loading effect (adjust time as needed)
            }
            dispose(); // Close the splash screen once loading is complete
            // Open the login UI after the splash screen is disposed
            SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
        } catch (InterruptedException e) {
            e.printStackTrace(); // Print the stack trace in case of an interruption
        }
    }
}