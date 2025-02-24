package services;

import database.DatabaseConnection;
import models.User;
import utils.PasswordUtil;

import java.sql.*;

public class UserService {

    public static boolean registerUser(String name, String email, String password, String role) {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);

        String query = "INSERT INTO users (name, email, password, salt, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, salt);
            stmt.setString(5, role);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static User loginUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String salt = rs.getString("salt");

                    // Ensure that storedHash and salt are not null and that the password matches
                    if (storedHash != null && salt != null && PasswordUtil.verifyPassword(password, storedHash, salt)) {
                        // Assuming User constructor requires id, name, email, role, and password
                        return new User(rs.getInt("id"), rs.getString("name"), email, rs.getString("role"), password);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during user login: " + e.getMessage());
            e.printStackTrace(); // Or use a logging framework
        }
        return null; // Return null if login fails

    }
}
