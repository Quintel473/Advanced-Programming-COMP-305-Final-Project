// Importing required packages and classes
package ui;

import models.Package;
import models.TrackingUpdate;
import models.User;
import services.PackageService;
import services.TrackingService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The DeliveryCompanyDashboardUI class represents the dashboard window
 * shown to a delivery company user. It allows them to:
 * - View all packages in the system
 * - Update the status of a package
 * - View the tracking history of a package
 * - Logout
 */
public class DeliveryCompanyDashboardUI extends JFrame {

    // The currently logged-in user (delivery company)
    private User user;

    // Text area where package information will be displayed
    private JTextArea packageListArea;

    /**
     * Constructor initializes the dashboard window for the delivery company
     * @param user The logged-in delivery company user
     */
    public DeliveryCompanyDashboardUI(User user) {
        this.user = user;

        // Set window title, size, close operation, and appearance
        setTitle("Delivery Company Dashboard - " + user.getRole());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setResizable(false);         // Prevent resizing

        // Main panel using BorderLayout for structured layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Title label at the top of the window
        JLabel titleLabel = new JLabel("Delivery Company Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Package list text area in the center with a scroll bar
        packageListArea = new JTextArea();
        packageListArea.setEditable(false); // Read-only
        packageListArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        packageListArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(packageListArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel at the bottom with 4 control buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        // Refresh button: reloads the list of packages
        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(e -> loadPackages());

        // Update status button: prompts user to change the package status
        JButton updateStatusButton = createStyledButton("Update Status");
        updateStatusButton.setBackground(new Color(255, 193, 7)); // Yellow
        updateStatusButton.addActionListener(e -> updatePackageStatus());

        // View history button: shows tracking updates for a selected package
        JButton viewTrackingButton = createStyledButton("View History");
        viewTrackingButton.setBackground(new Color(40, 167, 69)); // Green
        viewTrackingButton.addActionListener(e -> viewTrackingHistory());

        // Logout button: closes this dashboard and returns to the login screen
        JButton logoutButton = createStyledButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69)); // Red
        logoutButton.addActionListener(e -> {
            dispose(); // Close current window
            SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true)); // Open login screen
        });

        // Add buttons to the button panel
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(viewTrackingButton);
        buttonPanel.add(logoutButton);

        // Add the button panel to the main panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame and load the packages
        add(mainPanel);
        loadPackages(); // Load and display packages initially
        setVisible(true); // Show the window
    }

    /**
     * Creates a reusable styled JButton with consistent formatting
     * @param text Text to display on the button
     * @return A styled JButton
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(180, 40));
        button.setBackground(new Color(50, 150, 250)); // Default blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false); // Remove default border
        return button;
    }

    /**
     * Loads all packages from the backend and displays them in the text area
     */
    private void loadPackages() {
        try {
            // Fetch all packages
            List<Package> packages = PackageService.getAllPackages();
            packageListArea.setText(""); // Clear previous content

            // Loop through and display each package
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
        } catch (Exception e) {
            // Show error if packages couldn't be fetched
            JOptionPane.showMessageDialog(this, "Failed to load packages: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Allows the delivery company to update the status of a package
     * Prompts for the package ID and the new status
     */
    private void updatePackageStatus() {
        try {
            // Prompt user for package ID and new status
            String packageIdStr = JOptionPane.showInputDialog(this, "Enter Package ID:");
            String newStatus = JOptionPane.showInputDialog(this, "Enter New Status:");

            // Validate inputs
            if (packageIdStr == null || newStatus == null ||
                    packageIdStr.trim().isEmpty() || newStatus.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please try again.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int packageId = Integer.parseInt(packageIdStr.trim());

            // Update the package status via the service
            boolean packageUpdated = PackageService.updatePackageStatus(packageId, newStatus.trim());

            if (packageUpdated) {
                // Log the tracking update if status was updated
                boolean trackingLogged = TrackingService.addTrackingUpdate(packageId, newStatus.trim());

                if (trackingLogged) {
                    JOptionPane.showMessageDialog(this, "Package status updated and tracking logged successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Package updated, but failed to log tracking update.", "Warning", JOptionPane.WARNING_MESSAGE);
                }

                // Refresh package list
                loadPackages();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update package status.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            // Handle invalid number input
            JOptionPane.showMessageDialog(this, "Package ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // General error handling
            JOptionPane.showMessageDialog(this, "Error updating package status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Prompts for a package ID and displays its full tracking history
     */
    private void viewTrackingHistory() {
        try {
            // Prompt user for the package ID
            String packageIdStr = JOptionPane.showInputDialog(this, "Enter Package ID to view tracking history:");

            // Exit if input was cancelled or empty
            if (packageIdStr == null || packageIdStr.trim().isEmpty()) {
                return;
            }

            int packageId = Integer.parseInt(packageIdStr.trim());

            // Fetch tracking history
            List<TrackingUpdate> history = TrackingService.getTrackingHistory(packageId);

            // Show message if no history found
            if (history.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tracking history found for this package.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Build a text area showing each tracking update
            JTextArea historyArea = new JTextArea();
            historyArea.setEditable(false);
            for (TrackingUpdate update : history) {
                historyArea.append("Status: " + update.getStatus() + " | Time: " + update.getUpdateTime() + "\n");
            }

            // Wrap it in a scrollable pane and display it
            JScrollPane scrollPane = new JScrollPane(historyArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Tracking History", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            // Handle invalid number input
            JOptionPane.showMessageDialog(this, "Package ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Handle backend/service error
            JOptionPane.showMessageDialog(this, "Error fetching tracking history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
