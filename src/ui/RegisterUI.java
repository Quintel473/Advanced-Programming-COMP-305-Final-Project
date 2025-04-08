package ui;

// Import UserService, which likely contains the method for registering a user
import services.UserService;

// Import necessary Swing and AWT classes for building the GUI
import javax.swing.*; // For Swing components like JFrame, JTextField, JPasswordField, JLabel, etc.
import java.awt.*;    // For layout management and colors
import java.awt.event.ActionEvent;    // For handling button click events
import java.awt.event.ActionListener; // For implementing ActionListener interface

// Define the RegisterUI class which extends JFrame
// JFrame is used as the primary window for the registration interface
public class RegisterUI extends JFrame {
    // Declare UI components as instance variables
    private JTextField nameField, emailField;        // Input fields for name and email
    private JPasswordField passwordField;           // Input field for password
    private JComboBox<String> roleComboBox;         // Dropdown menu for selecting user roles
    private JButton registerButton, backButton;     // Buttons for "Register" and "Back to Login"

    // Constructor for setting up the Register UI
    public RegisterUI() {
        setTitle("Package Tracking System - Register"); // Set the title of the window
        setSize(450, 350);                             // Set the dimensions of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the program when the window is closed
        setLocationRelativeTo(null);                   // Center the window on the screen
        setResizable(false);                           // Disable resizing of the window

        // Main Panel: container for all components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10)); // Use BorderLayout with spacing between components
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the panel
        mainPanel.setBackground(Color.WHITE);          // Set the panel's background to white

        // Title Label: displays "Register" at the top of the window
        JLabel titleLabel = new JLabel("Register", SwingConstants.CENTER); // Center-align the text
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font style and size
        mainPanel.add(titleLabel, BorderLayout.NORTH);       // Add the title to the top of the panel

        // Form Panel: contains input fields for user registration
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // 5 rows, 2 columns, with spacing
        formPanel.setBackground(Color.WHITE); // Set background to white

        // Add labels and input fields to the form
        formPanel.add(new JLabel("Name:"));            // Label for name input
        nameField = new JTextField();                 // Text field for name
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));          // Label for email input
        emailField = new JTextField();               // Text field for email
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));       // Label for password input
        passwordField = new JPasswordField();        // Password field for hiding input
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Role:"));           // Label for role selection
        String[] roles = {"Customer", "Delivery Company"}; // Define options for the dropdown
        roleComboBox = new JComboBox<>(roles);        // Dropdown menu for roles
        formPanel.add(roleComboBox);

        mainPanel.add(formPanel, BorderLayout.CENTER); // Add the form panel to the center of the main panel

        // Buttons Panel: contains the "Register" and "Back to Login" buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE); // Set the background to white

        registerButton = createStyledButton("Register"); // Create a styled "Register" button
        // Add an ActionListener to handle the button click
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser(); // Call the registerUser method when clicked
            }
        });

        backButton = createStyledButton("Back to Login"); // Create a styled "Back to Login" button
        backButton.setBackground(new Color(220, 53, 69)); // Set the button color to red
        // Add an ActionListener to navigate back to the login screen
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginUI loginUI = new LoginUI(); // Create an instance of the login UI
                loginUI.setVisible(true);        // Make the login window visible
                dispose();                       // Close the current window
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the bottom of the main panel

        add(mainPanel); // Add the main panel to the frame
        setVisible(true); // Make the frame visible
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text); // Create a button with the specified text
        button.setFont(new Font("SansSerif", Font.BOLD, 16)); // Set font style and size
        button.setPreferredSize(new Dimension(150, 40));     // Set button dimensions
        button.setBackground(new Color(50, 150, 250));       // Set background color (blue)
        button.setForeground(Color.WHITE);                   // Set text color to white
        button.setFocusPainted(false);                       // Remove focus painting
        return button;
    }

    // Method to handle user registration
    private void registerUser() {
        // Get input values from the fields
        String name = nameField.getText(); // Retrieve name
        String email = emailField.getText(); // Retrieve email
        String password = new String(passwordField.getPassword()); // Retrieve password as a string
        String role = (String) roleComboBox.getSelectedItem(); // Retrieve selected role from dropdown

        // Use the UserService to register the user and display a success or error message
        boolean success = UserService.registerUser(name, email, password, role);
        if (success) { // If registration succeeds
            JOptionPane.showMessageDialog(this, "Registration Successful! You can now login."); // Success message
            LoginUI loginUI = new LoginUI(); // Create instance of login UI
            loginUI.setVisible(true);       // Make login UI visible
            dispose();                      // Close the current registration window
        } else { // If registration fails
            JOptionPane.showMessageDialog(this, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE); // Error message
        }
    }

    // Main method to launch the RegisterUI
    public static void main(String[] args) {
        new RegisterUI(); // Create an instance of RegisterUI to display the window
    }
}