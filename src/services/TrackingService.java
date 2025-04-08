package services;

import database.DatabaseConnection;
import models.TrackingUpdate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TrackingService handles operations related to tracking updates for packages.
 * Includes adding new tracking updates and retrieving tracking history.
 */
public class TrackingService {

    /**
     * Adds a tracking update to the database for a specific package.
     *
     * @param packageId ID of the package being updated.
     * @param status    The status message (e.g., "In Transit", "Delivered", etc.).
     * @return true if the update was successfully inserted, false otherwise.
     */
    public static boolean addTrackingUpdate(int packageId, String status) {
        // SQL query to insert a new tracking update
        String query = "INSERT INTO tracking_updates (package_id, status) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters for package ID and status
            stmt.setInt(1, packageId);
            stmt.setString(2, status);

            // Execute the insert and return true if successful
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // Log SQL errors to console
        }

        return false; // Return false if insertion failed
    }

    /**
     * Retrieves the full tracking history for a given package ID.
     *
     * @param packageId ID of the package.
     * @return A list of TrackingUpdate objects, each representing a status update.
     */
    public static List<TrackingUpdate> getTrackingHistory(int packageId) {
        List<TrackingUpdate> updates = new ArrayList<>();

        // SQL query to retrieve all tracking updates for a package
        String query = "SELECT * FROM tracking_updates WHERE package_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, packageId); // Set the package ID

            ResultSet rs = stmt.executeQuery();

            // Loop through each row in the result set and build a TrackingUpdate object
            while (rs.next()) {
                updates.add(new TrackingUpdate(
                        rs.getInt("id"),                 // Unique tracking update ID
                        rs.getInt("package_id"),         // Package ID
                        rs.getString("status"),          // Status text
                        rs.getTimestamp("update_time")   // Timestamp of update
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log SQL errors to console
        }

        return updates; // Return the full list of updates
    }
}
