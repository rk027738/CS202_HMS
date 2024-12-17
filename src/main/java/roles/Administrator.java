package roles;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import static DatabaseUtils.*;

public class Administrator {
    private Scanner scanner = new Scanner(System.in);

    public void menu() {
        while (true) {
            System.out.println("\n--- Administrator Menu ---");
            System.out.println("1. Add New Room");
            System.out.println("2. Modify Room Details");
            System.out.println("3. Delete Room");
            System.out.println("4. Generate Revenue Report");
            System.out.println("5. Return to Main Menu");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> addNewRoom();
                case 2 -> modifyRoomDetails();
                case 3 -> deleteRoom();
                case 4 -> generateRevenueReport();
                case 5 -> { return; }
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
        try (Connection conn = getConnection()) {
            int rows = executeUpdate(conn, query, roomNumber, roomType, price);
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

    private void generateRevenueReport() {
        String query = "SELECT SUM(Amount) AS TotalRevenue FROM Payment";
        try (Connection conn = getConnection();
             ResultSet rs = executeQuery(conn, query)) {
            if (rs.next()) {
                System.out.printf("Total Revenue: %.2f\n", rs.getDouble("TotalRevenue"));
            }
        } catch (SQLException e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }
}
