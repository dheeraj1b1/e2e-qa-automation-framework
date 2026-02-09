package com.qa.framework.utils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBUtil {

    // Connection Details (Injected via Jenkins/Env)
    private static final String DB_URL = System.getenv("DB_URL") != null ? System.getenv("DB_URL")
            : "jdbc:mysql://localhost:3306/automation_db";
    private static final String DB_USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "qa_user";
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    // Helper to get a connection
    public static Connection getConnection() throws SQLException {
        if (DB_PASSWORD == null) {
            throw new SQLException("DB_PASSWORD environment variable is not set!");
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Method to fetch a single user's details by username
    public static Map<String, String> getUserData(String username) {
        Map<String, String> data = new HashMap<>();
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                data.put("id", rs.getString("id"));
                data.put("username", rs.getString("username"));
                data.put("email", rs.getString("email"));
                data.put("role", rs.getString("role"));
                data.put("is_active", rs.getString("is_active")); // Returns "1" or "0"
            }
        } catch (SQLException e) {
            LoggerUtil.logError("DB Query Failed: " + e.getMessage());
        }
        return data; // Returns empty map if not found
    }

    // Method to check if a record exists
    public static boolean isUserPresent(String username) {
        return !getUserData(username).isEmpty();
    }
}