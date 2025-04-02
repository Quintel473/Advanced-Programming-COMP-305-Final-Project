package services;

import database.DatabaseConnection;
import models.Package;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackageService {

    // Add Package
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

    // Get User Packages
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

    // Get All Packages
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

    // Update Package Status
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

    // Delete Package
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