package ui;

import models.Package;
import models.User;
import services.PackageService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * AdminDashboardUI provides the admin user with a full list of packages
 * and controls to add or delete packages from the system.
 */
public class AdminDashboardUI extends JFrame {
    private User user; // Logged-in user (admin)
    private JTextArea packageListArea; // Display area for package information

    /**
     * Constructor to initialize the admin dashboard.
     */
    public AdminDashboardUI(User user) {
        this.user = user;

        // Set window properties
        setTitle("Admin Dashboard - " + user.getRole());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create the main panel with padding and background color
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Title label at the top
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Text area to display the list of packages
        packageListArea = new JTextArea();
        packageListArea.setEditable(false);
        packageListArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        packageListArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Scroll pane for the text area
        JScrollPane scrollPane = new JScrollPane(packageListArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel for actions
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        // Button to refresh package list
        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(e -> loadPackages());

        // Button to add a package
        JButton addPackageButton = createStyledButton("Add");
        addPackageButton.setBackground(new Color(40, 167, 69)); // Green color
        addPackageButton.addActionListener(e -> addPackage());

        // Button to delete a package
        JButton deletePackageButton = createStyledButton("Delete");
        deletePackageButton.setBackground(new Color(255, 80, 80)); // Red color
        deletePackageButton.addActionListener(e -> deletePackage());

        // Button to log out
        JButton logoutButton = createStyledButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69)); // Dark red
        logoutButton.addActionListener(e -> {
            dispose(); // Close this window
            SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true)); // Return to login
        });

        // Add all buttons to the button panel
        buttonPanel.add(refreshButton);
        buttonPanel.add(addPackageButton);
        buttonPanel.add(deletePackageButton);
        buttonPanel.add(logoutButton);

        // Add button panel to the bottom of the main panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);

        // Load the package list on startup
        loadPackages();

        setVisible(true); // Show the window
    }

    /**
     * Utility method to create a styled JButton with consistent formatting.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(50, 150, 250)); // Default blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false); // Remove focus outline
        return button;
    }

    /**
     * Loads all packages from the service and displays them in the text area.
     */
    private void loadPackages() {
        try {
            List<Package> packages = PackageService.getAllPackages(); // Get all packages
            packageListArea.setText(""); // Clear the text area

            // Append each package's data to the display
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
            JOptionPane.showMessageDialog(this, "Failed to load packages: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens input dialogs to gather new package information and adds the package.
     */
    private void addPackage() {
        try {
            String trackingNumber = JOptionPane.showInputDialog(this, "Enter Tracking Number:");
            String senderIdStr = JOptionPane.showInputDialog(this, "Enter Sender ID:");
            String recipientIdStr = JOptionPane.showInputDialog(this, "Enter Recipient ID:");

            // Validate input
            if (trackingNumber != null && !trackingNumber.trim().isEmpty() &&
                    senderIdStr != null && !senderIdStr.trim().isEmpty() &&
                    recipientIdStr != null && !recipientIdStr.trim().isEmpty()) {

                int senderId = Integer.parseInt(senderIdStr);
                int recipientId = Integer.parseInt(recipientIdStr);

                boolean success = PackageService.addPackage(trackingNumber, senderId, recipientId);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Package added successfully!");
                    loadPackages(); // Refresh package list
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add package.");
                }

            } else {
                JOptionPane.showMessageDialog(this, "Invalid input. Please try again.", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Sender and Recipient IDs must be integers.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding package: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Prompts for a package ID and deletes the corresponding package.
     */
    private void deletePackage() {
        try {
            String packageIdStr = JOptionPane.showInputDialog(this, "Enter Package ID to Delete:");

            // Validate input
            if (packageIdStr != null && !packageIdStr.trim().isEmpty()) {
                int packageId = Integer.parseInt(packageIdStr);

                boolean success = PackageService.deletePackage(packageId);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Package deleted successfully!");
                    loadPackages(); // Refresh package list
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete package. It may have associated tracking updates.");
                }

            } else {
                JOptionPane.showMessageDialog(this, "Invalid input. Please try again.", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Package ID must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting package: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
