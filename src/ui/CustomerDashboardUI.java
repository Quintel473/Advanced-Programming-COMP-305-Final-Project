package ui;

import models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class provides the dashboard interface for a Customer user.
 * From here, the user can access package management or log out.
 */
public class CustomerDashboardUI extends JFrame {
    private final User user; // Represents the currently logged-in user

    /**
     * Constructor to initialize the Customer Dashboard UI.
     */
    public CustomerDashboardUI(User user) {
        this.user = user; // Assign the passed-in user

        // Window setup
        setTitle("Dashboard - " + user.getName()); // Set the window title with user's name
        setSize(700, 500); // Fixed window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only dispose this window on close
        setLocationRelativeTo(null); // Center the window on screen
        setResizable(false); // Prevent resizing

        // Main panel with border layout and padding
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE); // White background

        // Title label welcoming the user
        JLabel titleLabel = new JLabel("Welcome, " + user.getName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(titleLabel, BorderLayout.NORTH); // Add label at the top

        // Button panel with GridLayout (2 buttons vertically stacked)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10)); // 2 rows, 1 column
        buttonPanel.setBackground(Color.WHITE);

        // Button to open the Package Management UI
        JButton managePackagesButton = createStyledButton("📦 Manage Packages");
        managePackagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the CustomerPackageManagementUI window
                new CustomerPackageManagementUI(user).setVisible(true);
            }
        });

        // Button to log out and return to login screen
        JButton logoutButton = createStyledButton("🚪 Logout");
        logoutButton.setBackground(new Color(220, 53, 69)); // Red color to indicate danger/log out
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close this window
                // Open LoginUI on a new thread to avoid blocking
                SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
            }
        });

        // Add both buttons to the button panel
        buttonPanel.add(managePackagesButton);
        buttonPanel.add(logoutButton);

        // Place the button panel in the center of the main panel
        panel.add(buttonPanel, BorderLayout.CENTER);

        // Add the main panel to the JFrame
        add(panel);
    }

    /**
     * Utility method to create a consistently styled button.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(250, 50));
        button.setBackground(new Color(50, 150, 250)); // Blue background
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false); // No border when focused
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20)); // Padding inside the button
        return button;
    }

    /**
     * Main method (for testing or debugging only).
     * This initializes the UI with Nimbus look and feel.
     */
    public static void main(String[] args) {
        try {
            // Apply Nimbus look for a modern UI style
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace(); // Print error if look and feel fails
        }

        // Normally, you would launch this UI after login with a real User
        // new CustomerDashboardUI(new User(...)).setVisible(true);
    }
}
