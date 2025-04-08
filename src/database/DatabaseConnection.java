package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database URL with the schema name 'package_tracking_system'
    private static final String URL = "jdbc:mysql://localhost:3306/package_tracking_system";

    // MySQL user for connecting to the database
    private static final String USER = "root";  // Change if your MySQL has a different user

    // Password for the MySQL user
    private static final String PASSWORD = "";  // Change if you set a password

    // Method to establish and return a connection to the database
    public static Connection getConnection() throws SQLException {
        // Use DriverManager to get a connection to the MySQL database
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
