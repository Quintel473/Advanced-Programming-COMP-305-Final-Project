package ui;

import models.Package;
import models.User;
import services.PackageService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DeliveryCompanyDashboardUI extends JFrame {
    private User user;
    private JTextArea packageListArea;

    public DeliveryCompanyDashboardUI(User user) {
        this.user = user;
        setTitle("Delivery Company Dashboard - " + user.getRole());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Title Label
        JLabel titleLabel = new JLabel("Delivery Company Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Package List Area
        packageListArea = new JTextArea();
        packageListArea.setEditable(false);
        packageListArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        packageListArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(packageListArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        // Refresh Button
        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(e -> loadPackages());

        // Update Status Button
        JButton updateStatusButton = createStyledButton("Update Status");
        updateStatusButton.setBackground(new Color(255, 193, 7)); // Yellow color for Update Status
        updateStatusButton.addActionListener(e -> updatePackageStatus());

        // Logout Button
        JButton logoutButton = createStyledButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69)); // Red color for Logout
        logoutButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true)); // Reopen login page
        });

        // Add Buttons
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(logoutButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadPackages();
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void loadPackages() {
        try {
            List<Package> packages = PackageService.getAllPackages(); // Fetch all packages
            packageListArea.setText(""); // Clear the text area
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

    private void updatePackageStatus() {
        try {
            String packageIdStr = JOptionPane.showInputDialog(this, "Enter Package ID:");
            String newStatus = JOptionPane.showInputDialog(this, "Enter New Status:");
            if (packageIdStr != null && !packageIdStr.trim().isEmpty() && newStatus != null && !newStatus.trim().isEmpty()) {
                int packageId = Integer.parseInt(packageIdStr);
                boolean success = PackageService.updatePackageStatus(packageId, newStatus);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Package status updated successfully!");
                    loadPackages(); // Refresh the list
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update package status.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid input. Please try again.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Package ID must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating package status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}