package ui;

import models.Package;
import models.User;
import services.PackageService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminDashboardUI extends JFrame {
    private User user;
    private JTextArea packageListArea;

    public AdminDashboardUI(User user) {
        this.user = user;
        setTitle("Admin Dashboard - " + user.getRole());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        packageListArea = new JTextArea();
        packageListArea.setEditable(false);
        packageListArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        packageListArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(packageListArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(e -> loadPackages());

        JButton addPackageButton = createStyledButton("Add");
        addPackageButton.setBackground(new Color(40, 167, 69)); // Green color for Add Package
        addPackageButton.addActionListener(e -> addPackage());

        JButton deletePackageButton = createStyledButton("Delete");
        deletePackageButton.setBackground(new Color(255, 80, 80)); // Red color for Delete Package
        deletePackageButton.addActionListener(e -> deletePackage());

        JButton logoutButton = createStyledButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69)); // Red color for Logout
        logoutButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true)); // Reopen login page
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(addPackageButton);
        buttonPanel.add(deletePackageButton);
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

    private void addPackage() {
        try {
            String trackingNumber = JOptionPane.showInputDialog(this, "Enter Tracking Number:");
            String senderIdStr = JOptionPane.showInputDialog(this, "Enter Sender ID:");
            String recipientIdStr = JOptionPane.showInputDialog(this, "Enter Recipient ID:");
            if (trackingNumber != null && !trackingNumber.trim().isEmpty() &&
                    senderIdStr != null && !senderIdStr.trim().isEmpty() &&
                    recipientIdStr != null && !recipientIdStr.trim().isEmpty()) {
                int senderId = Integer.parseInt(senderIdStr);
                int recipientId = Integer.parseInt(recipientIdStr);
                boolean success = PackageService.addPackage(trackingNumber, senderId, recipientId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Package added successfully!");
                    loadPackages(); // Refresh the list
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

    private void deletePackage() {
        try {
            String packageIdStr = JOptionPane.showInputDialog(this, "Enter Package ID to Delete:");
            if (packageIdStr != null && !packageIdStr.trim().isEmpty()) {
                int packageId = Integer.parseInt(packageIdStr);
                boolean success = PackageService.deletePackage(packageId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Package deleted successfully!");
                    loadPackages(); // Refresh the list
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