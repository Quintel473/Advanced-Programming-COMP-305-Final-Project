package ui;

// Import necessary classes and dependencies
import services.UserService; // Used to authenticate users
import models.User;          // Represents user model, storing user details such as role

import javax.swing.*;  // For Swing components like JFrame, JTextField, JLabel, JPasswordField
import java.awt.*;     // For GUI layout and styling

// Define the LoginUI class, extending JFrame for the main application window
public class LoginUI extends JFrame {
    // Declare UI components as instance variables
    private JTextField emailField;         // Input field for the user's email
    private JPasswordField passwordField; // Input field for the user's password (hidden for security)

    // Constructor to initialize and set up the login UI
    public LoginUI() {
        setTitle("Login");               // Set the title of the frame
        setSize(400, 300);               // Define the frame dimensions (400x300 pixels)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application when window is closed
        setLocationRelativeTo(null);     // Center the frame on the screen
        setResizable(false);             // Disable resizing of the window

        // Create the main container panel with padding and layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10)); // Use BorderLayout with spacing
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        panel.setBackground(Color.WHITE); // Set the background color to white

        // Title Label: displays "Login" at the top
        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER); // Center-align the text
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));           // Set font style and size
        panel.add(titleLabel, BorderLayout.NORTH);                     // Add the label at the top

        // Form Panel: contains input fields for email and password
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns with spacing
        formPanel.setBackground(Color.WHITE); // Match the background color

        // Add labels and input fields for email and password
        formPanel.add(new JLabel("Email:"));         // Label for email input
        emailField = new JTextField();              // Text field for entering email
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));      // Label for password input
        passwordField = new JPasswordField();        // Password field (hidden input)
        formPanel.add(passwordField);

        panel.add(formPanel, BorderLayout.CENTER);  // Add the form panel to the center of the main panel

        // Buttons Panel: contains the "Login" and "Register" buttons
        JPanel buttonPanel = new JPanel();          // Create a panel to hold buttons
        buttonPanel.setBackground(Color.WHITE);     // Match the background color

        // Create the "Login" button and attach an ActionListener to handle the login logic
        JButton loginButton = createStyledButton("Login");
        loginButton.addActionListener(e -> handleLogin());

        // Create the "Register" button to navigate to the registration screen
        JButton registerButton = createStyledButton("Register");
        registerButton.setBackground(new Color(40, 167, 69)); // Green color for register button
        registerButton.addActionListener(e -> {
            dispose(); // Close the current login UI
            SwingUtilities.invokeLater(() -> new RegisterUI().setVisible(true)); // Show registration UI
        });

        // Add both buttons to the button panel
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        panel.add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the bottom of the main panel

        add(panel); // Add the main panel to the frame
    }

    // Helper method to create and style buttons uniformly
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);               // Create button with specified text
        button.setFont(new Font("SansSerif", Font.BOLD, 14)); // Set font style and size
        button.setPreferredSize(new Dimension(150, 40));  // Define button dimensions
        button.setBackground(new Color(50, 150, 250));    // Blue background color
        button.setForeground(Color.WHITE);                // White text color
        button.setFocusPainted(false);                    // Remove focus highlight
        return button;
    }

    // Method to handle user login
    private void handleLogin() {
        // Retrieve user inputs
        String email = emailField.getText().trim();             // Get email input, trim spaces
        String password = new String(passwordField.getPassword()).trim(); // Get password input, trim spaces

        User user = UserService.loginUser(email, password); // Authenticate user via UserService
        if (user != null) { // If login is successful
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE); // Show success message
            dispose(); // Close the login UI

            // Determine the user's role and navigate to the appropriate dashboard
            SwingUtilities.invokeLater(() -> {
                if ("Admin".equalsIgnoreCase(user.getRole())) { // Admin role
                    new AdminDashboardUI(user).setVisible(true);
                } else if ("Customer".equalsIgnoreCase(user.getRole())) { // Customer role
                    new CustomerDashboardUI(user).setVisible(true);
                } else if ("Delivery Company".equalsIgnoreCase(user.getRole())) { // Delivery Company role
                    new DeliveryCompanyDashboardUI(user).setVisible(true);
                } else { // Handle unknown role
                    JOptionPane.showMessageDialog(null, "Role not recognized!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else { // If login fails
            JOptionPane.showMessageDialog(this, "Invalid email or password!", "Error", JOptionPane.ERROR_MESSAGE); // Show error message
        }
    }

    // Main method to launch the LoginUI
    public static void main(String[] args) {
        try {
            // Set the Nimbus look-and-feel for a modern appearance
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace(); // Print error if Nimbus look-and-feel fails
        }

        // Launch the login UI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}