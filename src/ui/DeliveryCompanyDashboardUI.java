package ui;

import models.Package;
import models.TrackingUpdate;
import models.User;
import services.PackageService;
import services.TrackingService;

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

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Delivery Company Dashboard", SwingConstants.CENTER);
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

        JButton updateStatusButton = createStyledButton("Update Status");
        updateStatusButton.setBackground(new Color(255, 193, 7));
        updateStatusButton.addActionListener(e -> updatePackageStatus());

        JButton viewTrackingButton = createStyledButton("View History");
        viewTrackingButton.setBackground(new Color(40, 167, 69)); // Green
        viewTrackingButton.addActionListener(e -> viewTrackingHistory());

        JButton logoutButton = createStyledButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(viewTrackingButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadPackages();
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(180, 40));
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void loadPackages() {
        try {
            List<Package> packages = PackageService.getAllPackages();
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load packages: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePackageStatus() {
        try {
            String packageIdStr = JOptionPane.showInputDialog(this, "Enter Package ID:");
            String newStatus = JOptionPane.showInputDialog(this, "Enter New Status:");

            if (packageIdStr == null || newStatus == null ||
                    packageIdStr.trim().isEmpty() || newStatus.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please try again.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int packageId = Integer.parseInt(packageIdStr.trim());
            boolean packageUpdated = PackageService.updatePackageStatus(packageId, newStatus.trim());

            if (packageUpdated) {
                boolean trackingLogged = TrackingService.addTrackingUpdate(packageId, newStatus.trim());
                if (trackingLogged) {
                    JOptionPane.showMessageDialog(this, "Package status updated and tracking logged successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Package updated, but failed to log tracking update.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                loadPackages();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update package status.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Package ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating package status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewTrackingHistory() {
        try {
            String packageIdStr = JOptionPane.showInputDialog(this, "Enter Package ID to view tracking history:");
            if (packageIdStr == null || packageIdStr.trim().isEmpty()) {
                return;
            }

            int packageId = Integer.parseInt(packageIdStr.trim());
            List<TrackingUpdate> history = TrackingService.getTrackingHistory(packageId);

            if (history.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tracking history found for this package.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JTextArea historyArea = new JTextArea();
            historyArea.setEditable(false);
            for (TrackingUpdate update : history) {
                historyArea.append("Status: " + update.getStatus() + " | Time: " + update.getUpdateTime() + "\n");
            }

            JScrollPane scrollPane = new JScrollPane(historyArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Tracking History", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Package ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching tracking history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
