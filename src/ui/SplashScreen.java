package ui;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    private JProgressBar progressBar;

    public SplashScreen() {
        // Set up splash screen window
        setSize(600, 300);
        setLocationRelativeTo(null); // Center the splash screen
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Add a gradient background
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(50, 150, 250); // Blue
                Color color2 = new Color(100, 200, 250); // Light Blue
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Add a centered welcome message
        JLabel titleLabel = new JLabel("Welcome to the Package Tracking System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.CENTER);

        // Add progress bar at the bottom
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        progressBar.setBackground(Color.WHITE);
        progressBar.setForeground(new Color(50, 150, 250));
        mainPanel.add(progressBar, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        // Simulate loading progress
        loadProgress();
    }

    private void loadProgress() {
        try {
            for (int i = 0; i <= 100; i++) {
                progressBar.setValue(i);
                Thread.sleep(30); // Simulates loading (adjust time as needed)
            }
            dispose(); // Close splash screen
            SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true)); // Open LoginUI
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}