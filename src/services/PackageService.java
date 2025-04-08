package services;

import database.DatabaseConnection;
import models.Package;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PackageService handles CRUD operations for packages,
 * including adding, retrieving, updating status, and deleting packages.
 */
public class PackageService {

    /**
     * Adds a new package to the database with an initial status of 'Pending'.
     *
     * @param trackingNumber Unique tracking number for the package.
     * @param senderId       ID of the sender user.
     * @param recipientId    ID of the recipient user.
     * @return true if the insert was successful, false otherwise.
     */
    public static boolean addPackage(String trackingNumber, int senderId, int recipientId) {
        String query = "INSERT INTO packages (tracking_number, sender_id, recipient_id, status) VALUES (?, ?, ?, 'Pending')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, trackingNumber);
            stmt.setInt(2, senderId);
            stmt.setInt(3, recipientId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error adding package: " + e.getMessage());
        }

        return false;
    }

    /**
     * Retrieves all packages where the given user is either the sender or recipient.
     *
     * @param userId ID of the user.
     * @return List of Package objects associated with the user.
     */
    public static List<Package> getUserPackages(int userId) {
        List<Package> packages = new ArrayList<>();
        String query = "SELECT * FROM packages WHERE sender_id = ? OR recipient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                packages.add(new Package(
                        rs.getInt("id"),
                        rs.getString("tracking_number"),
                        rs.getInt("sender_id"),
                        rs.getInt("recipient_id"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching packages: " + e.getMessage());
        }

        return packages;
    }

    /**
     * Retrieves all packages in the system (Admin view).
     *
     * @return List of all Package objects.
     */
    public static List<Package> getAllPackages() {
        List<Package> packages = new ArrayList<>();
        String query = "SELECT * FROM packages";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                packages.add(new Package(
                        rs.getInt("id"),
                        rs.getString("tracking_number"),
                        rs.getInt("sender_id"),
                        rs.getInt("recipient_id"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching all packages: " + e.getMessage());
        }

        return packages;
    }

    /**
     * Updates the status of a specific package (e.g., from "Pending" to "In Transit").
     *
     * @param packageId ID of the package to update.
     * @param newStatus The new status to set.
     * @return true if the update was successful, false otherwise.
     */
    public static boolean updatePackageStatus(int packageId, String newStatus) {
        String query = "UPDATE packages SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, packageId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating package status: " + e.getMessage());
        }

        return false;
    }

    /**
     * Deletes a package from the system.
     * Note: This may fail if the package has foreign key references in tracking_updates.
     *
     * @param packageId ID of the package to delete.
     * @return true if the delete was successful, false otherwise.
     */
    public static boolean deletePackage(int packageId) {
        String query = "DELETE FROM packages WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, packageId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting package: " + e.getMessage());
        }

        return false;
    }
}
