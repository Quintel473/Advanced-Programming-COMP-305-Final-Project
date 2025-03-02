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

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Title Label
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
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

        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(e -> loadPackages());

        JButton updateStatusButton = createStyledButton("Update Status");
        updateStatusButton.setBackground(new Color(255, 193, 7)); // Yellow color for Update Status
        updateStatusButton.addActionListener(e -> {
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
            }
        });

        // Add Buttons
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateStatusButton);
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
        List<Package> packages = PackageService.getUserPackages(user.getId());
        packageListArea.setText("");
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
}
