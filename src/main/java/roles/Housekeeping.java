package roles;

import utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import static utils.DatabaseUtils.*;

public class Housekeeping {
    private Scanner scanner = new Scanner(System.in);

    public void menu() {
        while (true) {
            System.out.println("\n--- Housekeeping Menu ---");
            System.out.println("1. View Cleaning Schedules");
            System.out.println("2. Update Cleaning Status");
            System.out.println("3. Logout");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> viewCleaningSchedules();
                case 2 -> updateCleaningStatus();
                case 3 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void viewCleaningSchedules() {
        String query = "SELECT h.HKSId, r.RoomNumber, h.ScheduleDate, h.HKStatus " +
                "FROM HousekeepingSchedule h " +
                "JOIN Room r ON h.RoomId = r.RoomId";
        try (Connection conn = DatabaseUtils.getConnection();
             ResultSet rs = DatabaseUtils.executeQuery(conn, query)) {
            while (rs.next()) {
                System.out.printf("Schedule ID: %d | Room: %s | Date: %s | Status: %s\n",
                        rs.getInt("HKSId"), rs.getString("RoomNumber"), rs.getDate("ScheduleDate"), rs.getString("HKStatus"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching schedules: " + e.getMessage());
        }
    }


    private void updateCleaningStatus() {
        System.out.print("Enter Room ID: ");
        int roomId = scanner.nextInt();
        System.out.print("Enter New Status (Scheduled/Completed): ");
        String status = scanner.next();

        String query = "UPDATE HousekeepingSchedule SET HKStatus = ? WHERE RoomId = ?";
        try (Connection conn = getConnection()) {
            int rows = executeUpdate(conn, query, status, roomId);
            if (rows > 0) System.out.println("Cleaning status updated.");
        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
        }
    }
}
