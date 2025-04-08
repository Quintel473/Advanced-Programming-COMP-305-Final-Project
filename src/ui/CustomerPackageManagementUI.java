package ui;

// Importing required model and service classes
import models.Package;
import models.User;
import services.PackageService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * This class represents the Package Management interface for a Customer user.
 * It allows viewing packages, adding new ones, and navigating back to the dashboard.
 */
public class CustomerPackageManagementUI extends JFrame {
    private User user;                      // The currently logged-in user
    private JTextArea packageListArea;      // Text area to display package information

    /**
     * Constructor to initialize the UI with the provided user.
     */
    public CustomerPackageManagementUI(User user) {
        this.user = user;

        // Set up the basic window properties
        setTitle("Package Management - " + user.getRole());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create the main content panel with padding and white background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Title label displayed at the top of the window
        JLabel titleLabel = new JLabel("Manage Packages", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Text area to display the package list
        packageListArea = new JTextArea();
        packageListArea.setEditable(false); // Make it read-only
        packageListArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        packageListArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Add the text area to a scroll pane and place it in the center
        JScrollPane scrollPane = new JScrollPane(packageListArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel for the action buttons (Refresh, Add, Dashboard)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        // Refresh Button - Reloads the package list from the database
        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(e -> loadPackages());

        // Add Package Button - Prompts user for package details and adds a new package
        JButton addButton = createStyledButton("Add Package");
        addButton.setBackground(new Color(40, 167, 69)); // Green color for emphasis
        addButton.addActionListener(e -> {
            String trackingNumber = JOptionPane.showInputDialog(this, "Enter Tracking Number:");
            int recipientId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Recipient ID:"));

            // Check that tracking number is not empty
            if (trackingNumber != null && !trackingNumber.trim().isEmpty()) {
                boolean success = PackageService.addPackage(trackingNumber, user.getId(), recipientId);

                // Show message based on result and reload list if successful
                if (success) {
                    JOptionPane.showMessageDialog(this, "Package added successfully!");
                    loadPackages();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add package.");
                }
            }
        });

        // Dashboard Button - Navigates back to the appropriate dashboard and closes this UI
        JButton backButton = createStyledButton("Dashboard");
        backButton.setBackground(new Color(220, 53, 69)); // Red color for back/cancel action
        backButton.addActionListener(e -> {
            openDashboard();  // Open corresponding dashboard
            dispose();        // Close the current window
        });

        // Add all buttons to the panel
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        // Add button panel to the bottom of the main panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the JFrame
        add(mainPanel);

        // Load packages initially when the UI opens
        loadPackages();

        // Make the UI visible
        setVisible(true);
    }

    /**
     * Creates a styled JButton with common styling properties.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(50, 150, 250)); // Default blue color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Loads packages associated with the current user and displays them in the text area.
     */
    private void loadPackages() {
        List<Package> packages = PackageService.getUserPackages(user.getId()); // Fetch user packages
        packageListArea.setText(""); // Clear previous text
        for (Package p : packages) {
            packageListArea.append(
                    "ID: " + p.getId() +
                            " | Tracking: " + p.getTrackingNumber() +
                            " | Sender ID: " + p.getSenderId() +
                            " | Recipient ID: " + p.getRecipientId() +
                            " | Status: " + p.getStatus() +
                            " | Created At: " + p.getCreatedAt() + "\n"
            );
        }
    }

    /**
     * Opens the appropriate dashboard UI based on the user's role.
     * If already opened, it brings that window to the front.
     */
    private void openDashboard() {
        Window[] windows = Window.getWindows(); // Get all open windows
        for (Window window : windows) {
            if (window instanceof CustomerDashboardUI) {
                window.toFront(); // Bring dashboard to front if already open
                return;
            }
        }

        // Create new dashboard if not found
        if (user.getRole().equals("Admin")) {
            new AdminDashboardUI(user);
        } else if (user.getRole().equals("Delivery Company")) {
            new DeliveryCompanyDashboardUI(user);
        }
    }
}
