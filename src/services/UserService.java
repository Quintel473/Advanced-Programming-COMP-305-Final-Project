package services;

import database.DatabaseConnection;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserService handles all user-related operations such as registration and login.
 */
public class UserService {

    /**
     * Registers a new user into the database.
     *
     * @param name     Full name of the user.
     * @param email    Email address of the user (used for login).
     * @param password Password in plain text (should be hashed in production).
     * @param role     Role of the user (e.g., Admin, Customer, Delivery).
     * @return true if the user is successfully registered, false otherwise.
     */
    public static boolean registerUser(String name, String email, String password, String role) {
        // SQL insert statement with placeholders
        String query = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        // Use try-with-resources to auto-close the database connection
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Bind parameters to the SQL query
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password); // In real systems, hash the password!
            stmt.setString(4, role);

            // Execute the query and return true if a row was inserted
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
        return false; // Return false on failure
    }

    /**
     * Logs in a user by verifying email and password.
     *
     * @param email    Email used during registration.
     * @param password Password (should match exactly as stored).
     * @return A User object if login is successful, or null if failed.
     */
    public static User loginUser(String email, String password) {
        // SQL query to find a user by email and password
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Bind email and password to the prepared statement
            stmt.setString(1, email);
            stmt.setString(2, password);

            // Execute query
            ResultSet rs = stmt.executeQuery();

            // If user exists, build and return the User object
            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),         // User ID
                        rs.getString("name"),    // Full Name
                        rs.getString("email"),   // Email
                        rs.getString("password"),// Password
                        rs.getString("role")     // User Role
                );

                System.out.println("Logged-in user: " + user); // Optional debug output
                return user;
            } else {
                // If no matching user, inform user (could be redirected to UI)
                System.out.println("Invalid credentials. Please try again.");
            }

        } catch (SQLException e) {
            System.err.println("Error logging in user: " + e.getMessage());
        }

        return null; // Return null if login fails
    }
}
