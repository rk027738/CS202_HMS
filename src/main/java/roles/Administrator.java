package roles;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import utils.DatabaseUtils;
import utils.DatabaseUtils.*;

import static utils.DatabaseUtils.*;

public class Administrator {
    private Scanner scanner = new Scanner(System.in);

    public void menu() {
        while (true) {
            System.out.println("\n--- Administrator Menu ---");
            System.out.println("1. Add New Room");
            System.out.println("2. Modify Room Details");
            System.out.println("3. Delete Room");
            System.out.println("4. Generate Revenue Report");
            System.out.println("5. View Housekeeping Performance");
            System.out.println("6. Logout");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> addNewRoom();
                case 2 -> modifyRoomDetails();
                case 3 -> deleteRoom();
                case 4 -> generateRevenueReport();
                case 5 -> viewHousekeepingPerformance();
                case 6 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void addNewRoom() {
        System.out.print("Enter Room Number: ");
        String roomNumber = scanner.next();
        System.out.print("Enter Room Type (Single/Double/Family): ");
        String roomType = scanner.next();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();

        String query = "INSERT INTO Room (RoomNumber, RoomType, Price, RoomStatus) VALUES (?, ?, ?, 'Available')";
        try (Connection conn = DatabaseUtils.getConnection()) {
            int rows = DatabaseUtils.executeUpdate(conn, query, roomNumber, roomType, price);
            if (rows > 0) System.out.println("Room added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding room: " + e.getMessage());
        }
    }


    private void modifyRoomDetails() {
        System.out.print("Enter Room Number to Modify: ");
        String roomNumber = scanner.next();
        System.out.print("Enter New Price: ");
        double newPrice = scanner.nextDouble();

        String query = "UPDATE Room SET Price = ? WHERE RoomNumber = ?";
        try (Connection conn = getConnection()) {
            int rows = executeUpdate(conn, query, newPrice, roomNumber);
            if (rows > 0) System.out.println("Room details updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error modifying room: " + e.getMessage());
        }
    }

    private void deleteRoom() {
        System.out.print("Enter Room Number to Delete: ");
        String roomNumber = scanner.next();

        String query = "DELETE FROM Room WHERE RoomNumber = ?";
        try (Connection conn = getConnection()) {
            int rows = executeUpdate(conn, query, roomNumber);
            if (rows > 0) System.out.println("Room deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
        }
    }

    public void generateRevenueReport() {
        String query = "SELECT r.RoomType, SUM(p.Amount) AS TotalRevenue " +
                "FROM Payment p " +
                "JOIN Booking b ON p.BookingId = b.BookingId " +
                "JOIN Room r ON b.RoomId = r.RoomId " +
                "GROUP BY r.RoomType " +
                "ORDER BY TotalRevenue DESC";

        try (Connection conn = DatabaseUtils.getConnection();
             ResultSet rs = DatabaseUtils.executeQuery(conn, query)) {
            System.out.println("\n--- Revenue Report by Room Type ---");
            System.out.printf("%-15s | %-15s\n", "Room Type", "Total Revenue");
            System.out.println("---------------------------------");

            while (rs.next()) {
                String roomType = rs.getString("RoomType");
                double totalRevenue = rs.getDouble("TotalRevenue");
                System.out.printf("%-15s | %-15.2f\n", roomType, totalRevenue);
            }
            System.out.println("---------------------------------");
        } catch (SQLException e) {
            System.err.println("Error fetching revenue report: " + e.getMessage());
        }
    }

    public void viewHousekeepingPerformance() {
        String query = "SELECT u.Name AS HousekeeperName, COUNT(h.HKSId) AS TasksCompleted " +
                "FROM HousekeepingSchedule h " +
                "JOIN User u ON h.UserId = u.UserId " +
                "WHERE h.HKStatus = 'Completed' " +
                "GROUP BY u.Name " +
                "ORDER BY TasksCompleted DESC";

        try (Connection conn = DatabaseUtils.getConnection();
             ResultSet rs = DatabaseUtils.executeQuery(conn, query)) {
            System.out.println("\n--- Housekeeping Performance Report ---");
            System.out.printf("%-20s | %-15s\n", "Housekeeper Name", "Tasks Completed");
            System.out.println("------------------------------------------");

            while (rs.next()) {
                String housekeeperName = rs.getString("HousekeeperName");
                int tasksCompleted = rs.getInt("TasksCompleted");
                System.out.printf("%-20s | %-15d\n", housekeeperName, tasksCompleted);
            }
            System.out.println("------------------------------------------");
        } catch (SQLException e) {
            System.err.println("Error fetching housekeeping performance: " + e.getMessage());
        }
    }
}
