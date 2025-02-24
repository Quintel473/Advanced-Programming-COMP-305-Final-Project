package services;

import database.DatabaseConnection;
import models.Package;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackageService {

    public static boolean addPackage(String trackingNumber, int senderId, int recipientId) {
        String query = "INSERT INTO packages (tracking_number, sender_id, recipient_id, status) VALUES (?, ?, ?, 'Pending')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, trackingNumber);
            stmt.setInt(2, senderId);
            stmt.setInt(3, recipientId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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
            e.printStackTrace();
        }
        return packages;
    }

    public static boolean updatePackageStatus(int packageId, String newStatus) {
        String query = "UPDATE packages SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, packageId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;


        
    }
}
