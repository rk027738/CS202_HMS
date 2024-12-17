package roles;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import static DatabaseUtils.*;

public class Receptionist {
    private Scanner scanner = new Scanner(System.in);

    public void menu() {
        while (true) {
            System.out.println("\n--- Receptionist Menu ---");
            System.out.println("1. View All Bookings");
            System.out.println("2. Confirm Booking");
            System.out.println("3. Manage Payments");
            System.out.println("4. Return to Main Menu");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> viewAllBookings();
                case 2 -> confirmBooking();
                case 3 -> managePayments();
                case 4 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void viewAllBookings() {
        String query = "SELECT * FROM Booking";
        try (Connection conn = getConnection();
             ResultSet rs = executeQuery(conn, query)) {
            while (rs.next()) {
                System.out.printf("Booking ID: %d | Room: %s | Guest: %s | Status: %s\n",
                        rs.getInt("BookingId"), rs.getString("RoomNumber"),
                        rs.getString("GuestName"), rs.getString("Status"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
        }
    }

    private void confirmBooking() {
        System.out.print("Enter Booking ID to Confirm: ");
        int bookingId = scanner.nextInt();

        String query = "UPDATE Booking SET Status = 'Confirmed' WHERE BookingId = ?";
        try (Connection conn = getConnection()) {
            int rows = executeUpdate(conn, query, bookingId);
            if (rows > 0) System.out.println("Booking confirmed successfully.");
        } catch (SQLException e) {
            System.err.println("Error confirming booking: " + e.getMessage());
        }
    }

    private void managePayments() {
        System.out.print("Enter Booking ID: ");
        int bookingId = scanner.nextInt();
        System.out.print("Enter Payment Amount: ");
        double amount = scanner.nextDouble();

        String query = "INSERT INTO Payment (BookingId, PaymentDate, PaymentMethod, Amount) VALUES (?, CURDATE(), 'Cash', ?)";
        try (Connection conn = getConnection()) {
            int rows = executeUpdate(conn, query, bookingId, amount);
            if (rows > 0) System.out.println("Payment recorded successfully.");
        } catch (SQLException e) {
            System.err.println("Error recording payment: " + e.getMessage());
        }
    }
}
