package utils;

import java.sql.*;

public class DatabaseUtils {
    private static final String URL = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7752655";
    private static final String USER = "sql7752655";
    private static final String PASSWORD = "3zFkUu13Sp";

    // Establish database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Close resources safely
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
        try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
        try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
    }

    // Execute SELECT queries
    public static ResultSet executeQuery(Connection conn, String query, Object... params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps.executeQuery();
    }

    // Execute INSERT/UPDATE/DELETE queries
    public static int executeUpdate(Connection conn, String query, Object... params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps.executeUpdate();
    }
}
