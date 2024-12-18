package roles;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import static utils.DatabaseUtils.*;


public class Receptionist {
    private Scanner scanner = new Scanner(System.in);

    public void menu() {
        while (true) {
            System.out.println("\n--- Receptionist Menu ---");
            System.out.println("1. View All Bookings");
            System.out.println("2. Confirm Booking");
            System.out.println("3. Manage Payments");
            System.out.println("4. Logout");
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
        String query = "SELECT b.BookingId, b.CheckInDate, b.CheckOutDate, b.NumberOfGuests, b.BookingStatus, " +
                "r.RoomNumber, u.Name AS GuestName " +
                "FROM Booking b " +
                "JOIN Room r ON b.RoomId = r.RoomId " +
                "JOIN User u ON b.UserId = u.UserId";

        try (Connection conn = getConnection();
             ResultSet rs = executeQuery(conn, query)) {
            System.out.println("\n--- All Bookings ---");
            while (rs.next()) {
                System.out.printf(
                        "Booking ID: %d | Room Number: %s | Guest Name: %s | Check-In: %s | Check-Out: %s | Guests: %d | Status: %s\n",
                        rs.getInt("BookingId"),
                        rs.getString("RoomNumber"),
                        rs.getString("GuestName"),
                        rs.getDate("CheckInDate"),
                        rs.getDate("CheckOutDate"),
                        rs.getInt("NumberOfGuests"),
                        rs.getString("BookingStatus")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
        }
    }

    private void confirmBooking() {
        System.out.print("Enter Booking ID to Confirm: ");
        int bookingId = scanner.nextInt();

        String query = "UPDATE Booking SET BookingStatus = 'Confirmed' WHERE BookingId = ?";
        try (Connection conn = getConnection()) {
            int rows = executeUpdate(conn, query, bookingId);
            if (rows > 0) System.out.println("Booking confirmed successfully.");
        } catch (SQLException e) {
            System.err.println("Error confirming booking: " + e.getMessage());
        }
    }

//    private void managePayments() {
//        System.out.print("Enter Booking ID: ");
//        int bookingId = scanner.nextInt();
//        System.out.print("Enter Payment Amount: ");
//        double amount = scanner.nextDouble();
//
//        String query = "INSERT INTO Payment (BookingId, PaymentDate, PaymentMethod, Amount) VALUES (?, CURDATE(), 'Cash', ?)";
//        try (Connection conn = getConnection()) {
//            int rows = executeUpdate(conn, query, bookingId, amount);
//            if (rows > 0) System.out.println("Payment recorded successfully.");
//        } catch (SQLException e) {
//            System.err.println("Error recording payment: " + e.getMessage());
//        }
//    }
private void managePayments() {
    System.out.print("Enter Booking ID: ");
    int bookingId = scanner.nextInt(); // Booking ID from the user
    scanner.nextLine(); // Consume newline

    System.out.print("Enter Payment Amount: ");
    double amount = scanner.nextDouble(); // Payment amount
    scanner.nextLine(); // Consume newline

    System.out.println("Select Payment Method:");
    System.out.println("1. CreditCard");
    System.out.println("2. Cash");
    System.out.println("3. Online");
    System.out.print("Enter your choice: ");
    int paymentChoice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    String paymentMethod;
    switch (paymentChoice) {
        case 1 -> paymentMethod = "CreditCard";
        case 2 -> paymentMethod = "Cash";
        case 3 -> paymentMethod = "Online";
        default -> {
            System.out.println("Invalid choice. Payment not processed.");
            return; // Exit the method for invalid choice
        }
    }

    String query = "INSERT INTO Payment (BookingId, PaymentDate, PaymentMethod, Amount) VALUES (?, CURDATE(), ?, ?)";
    try (Connection conn = getConnection()) {
        int rows = executeUpdate(conn, query, bookingId, paymentMethod, amount);
        if (rows > 0) {
            System.out.println("Payment recorded successfully.");
        } else {
            System.out.println("No matching booking found for Booking ID: " + bookingId);
        }
    } catch (SQLException e) {
        System.err.println("Error recording payment: " + e.getMessage());
    }
}


}
